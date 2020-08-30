package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.api.ReposRemoteDataSource
import com.overswayit.githubapi.db.ReposLocalDataSource
import com.overswayit.githubapi.entity.Repo
import kotlinx.coroutines.*
import retrofit2.Response

class DefaultReposRepository(
    private val reposRemoteDataSource: ReposRemoteDataSource,
    private val reposLocalDataSource: ReposLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReposRepository {

    override fun observeRepo(ownerLogin: String, name: String): LiveData<Repo> {
        return reposLocalDataSource.observeRepo(ownerLogin, name)
    }

    override fun observeRepos(ownerLogin: String): LiveData<List<Repo>> {
        return reposLocalDataSource.observeRepos(ownerLogin)
    }

    override fun fetchRepos(ownerLogin: String): Response<List<Repo>> {
        return reposRemoteDataSource.fetchRepos(ownerLogin).execute()
    }

    override suspend fun insert(repo: Repo) = withContext(ioDispatcher) {
        reposLocalDataSource.insert(repo)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        reposLocalDataSource.deleteAll()
    }

}