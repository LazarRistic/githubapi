package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPIService {

    @GET("user/repos")
    fun testCredentials(@Header("Authorization") authorization: String?): Call<Any>

    @GET("users/{login}")
    fun searchUser(
        @Header("Authorization") authorization: String?,
        @Path("login") login: String,
        @Query("sort") sort: String = "login",
        @Query("order") order: String = "desc"
    ): Call<User>
}