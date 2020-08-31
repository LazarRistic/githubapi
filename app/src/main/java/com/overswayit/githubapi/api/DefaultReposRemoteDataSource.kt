package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.Repo
import retrofit2.Call

class DefaultReposRemoteDataSource(private val gitHubAPIService: GitHubAPIService): ReposRemoteDataSource {

    override fun fetchRepos(ownerLogin: String): Call<List<Repo>> {
        return gitHubAPIService.fetchRepos(ownerLogin)
    }
}