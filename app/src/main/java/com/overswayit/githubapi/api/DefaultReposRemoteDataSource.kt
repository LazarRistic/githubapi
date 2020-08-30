package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.sharedprefs.SharedPreference
import retrofit2.Call

class DefaultReposRemoteDataSource(private val gitHubAPIService: GitHubAPIService): ReposRemoteDataSource {

    private val credentials = SharedPreference.getString("CREDENTIALS")

    override fun fetchRepos(ownerLogin: String): Call<List<Repo>> {
        return gitHubAPIService.fetchRepos(credentials, ownerLogin)
    }
}