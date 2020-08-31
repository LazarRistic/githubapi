package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import retrofit2.Call

class DefaultUsersRemoteDataSource(private val gitHubAPIService: GitHubAPIService) :
    UsersRemoteDataSource {

    override fun searchUser(login: String): Call<User> {
        return gitHubAPIService.searchUser(login)
    }
}