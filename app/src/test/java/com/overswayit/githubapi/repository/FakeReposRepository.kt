package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.overswayit.githubapi.entity.Repo
import retrofit2.Response

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeReposRepository : ReposRepository {

    private var reposServicesData: LinkedHashMap<String, Repo> = LinkedHashMap()

    private val observableRepo = MutableLiveData<Repo>()
    private val observableRepos = MutableLiveData<List<Repo>>()

    override fun observeRepo(ownerLogin: String, name: String): LiveData<Repo> {
        var fetchedRepo: Repo? = null

        for (repo in reposServicesData.values) {
            if (repo.owner.login == ownerLogin && repo.name == name) {
                fetchedRepo = repo
                break
            }
        }

        if (fetchedRepo == null) {
            observableRepo.postValue(null)
        } else {
            observableRepo.postValue(fetchedRepo)
        }

        return observableRepo
    }

    override fun observeRepos(ownerLogin: String): LiveData<List<Repo>> {
        val fetchedRepos = ArrayList<Repo>()

        for (repo in reposServicesData.values) {
            if (repo.owner.login == ownerLogin) {
                fetchedRepos.add(repo)
            }
        }

        observableRepos.postValue(fetchedRepos)

        return observableRepos
    }

    override fun fetchRepos(ownerLogin: String): Response<List<Repo>> {
        val fetchedRepos = ArrayList<Repo>()

        for (repo in reposServicesData.values) {
            if (repo.owner.login == ownerLogin) {
                fetchedRepos.add(repo)
            }
        }

        return Response.success(fetchedRepos.toList())
    }

    override suspend fun insert(vararg repos: Repo) {
        for (repo in repos) {
            if (!reposServicesData.containsKey(repo.name)) {
                addRepo(repo)
            }
        }
    }

    override suspend fun deleteAll() {
        reposServicesData.clear()
    }

    fun addRepo(repo: Repo) {
        reposServicesData[repo.name] = repo
    }
}