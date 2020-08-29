package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import retrofit2.Call

interface UsersRemoteDataSource {

    fun searchUsersByName(name: String): Call<UserSearchResponse>

    fun searchUser(login: String): Call<User>

}