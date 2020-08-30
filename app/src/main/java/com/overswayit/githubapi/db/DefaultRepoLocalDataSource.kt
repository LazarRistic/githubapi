package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.Repo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultRepoLocalDataSource internal constructor(
    private val repoDao: RepoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepoLocalDataSource {

    override fun observeRepo(ownerLogin: String, name: String): LiveData<Repo> =
        repoDao.observeRepoByLoginAndName(ownerLogin, name)

    override fun observeRepos(ownerLogin: String): LiveData<List<Repo>> =
        repoDao.observeRepositoriesByLogin(ownerLogin)

    override suspend fun insert(vararg repos: Repo) = withContext(ioDispatcher) {
        repoDao.insert()
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        repoDao.deleteAll()
    }
}