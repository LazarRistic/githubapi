package com.overswayit.githubapi.api

import com.google.gson.JsonArray
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.entity.User
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPIService {

    @GET("users/{login}")
    fun searchUser(
        @Path("login") login: String,
        @Query("sort") sort: String = "login",
        @Query("order") order: String = "desc"
    ): Single<User>

    @GET("users/{login}/repos")
    fun fetchRepos(
        @Path("login") login: String,
        @Query("sort") sort: String = "stargazers_count",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: String = "100",
        @Query("page") page: String = "1"
    ): Call<List<Repo>>

    @GET("repos/{login}/{repo}/commits")
    fun fetchCommits(
        @Path("login") login: String,
        @Path("repo") repo: String,
        @Query("per_page") perPage: String = "100",
        @Query("page") page: String = "1"
    ): Call<JsonArray>
}