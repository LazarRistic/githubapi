package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.Repo
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

    @GET("users/{login}/repos")
    fun fetchRepos(
        @Header("Authorization") authorization: String?,
        @Path("login") login: String,
        @Query("sort") sort: String = "stargazers_count",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: String = "100",
        @Query("page") page: String = "1"
    ): Call<List<Repo>>
}