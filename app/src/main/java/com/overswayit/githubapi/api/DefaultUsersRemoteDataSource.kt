package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.sharedprefs.SharedPreference
import retrofit2.Call

class DefaultUsersRemoteDataSource(private val gitHubAPIService: GitHubAPIService) :
    UsersRemoteDataSource {

    private val credentials = SharedPreference.getString("CREDENTIALS")

    override fun searchUsersByName(name: String): Call<UserSearchResponse> {
        return gitHubAPIService.searchUsers(credentials, name)
    }

    override fun searchUser(login: String): Call<User> {
        return gitHubAPIService.searchUser(credentials, login)
    }
}