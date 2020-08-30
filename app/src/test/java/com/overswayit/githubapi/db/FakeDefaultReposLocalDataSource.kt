package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.overswayit.githubapi.entity.Repo

class FakeDefaultReposLocalDataSource(private var repos: MutableList<Repo> = mutableListOf()) :
    ReposLocalDataSource {

    private val _reposQuery = MutableLiveData("")
    private val _repoQuery = MutableLiveData(Pair("", ""))

    private val reposList: LiveData<List<Repo>> = _reposQuery.map {
        val newRepo = ArrayList<Repo>()

        for (repo in repos) {
            if (repo.owner.login == it) {
                newRepo.add(repo)
            }
        }

        newRepo
    }

    private val repo: LiveData<Repo> = _repoQuery.map {
        val newRepo: MutableLiveData<Repo> = MutableLiveData()

        for (repo in repos) {
            if (repo.owner.login == it.first && repo.name == it.second) {
                newRepo.postValue(repo)
                break
            }
        }

        newRepo.value!!
    }

    override fun observeRepo(ownerLogin: String, name: String): LiveData<Repo> {
        _repoQuery.value = Pair(ownerLogin, name)

        return repo
    }

    override fun observeRepos(ownerLogin: String): LiveData<List<Repo>> {
        _reposQuery.value = ownerLogin

        return reposList
    }

    override suspend fun insert(repo: Repo) {
        checkIfExistsAndResponses(
            repo,
            { index, sameRepo ->
                this.repos.removeAt(index)
                this.repos.add(index, sameRepo)
            },
            { sameRepo -> this.repos.add(sameRepo) })
    }

    override suspend fun deleteAll() {
        repos.clear()
    }

    /**
     * Check if [newRepo] exists and if exists do fun [exists] and if not do fun [missing]
     *
     * @param [newRepo] check if exists in global MutableLists [repos]
     * @param [exists] if [Repo] exists in global MutableLists [repos] then do fun [exists]
     * @param [missing] if [Repo] does not exists in global MutableLists [repos] then do fun [missing]
     */
    private fun checkIfExistsAndResponses(
        newRepo: Repo,
        exists: (index: Int, repo: Repo) -> Unit,
        missing: (repo: Repo) -> Unit
    ) {
        var alreadyExists = false
        for (i in 0 until repos.size) {
            if (repos[i].name == newRepo.name) {
                exists(i, newRepo)
                alreadyExists = true
                break
            }
        }

        if (!alreadyExists) {
            missing(newRepo)
        }
    }
}