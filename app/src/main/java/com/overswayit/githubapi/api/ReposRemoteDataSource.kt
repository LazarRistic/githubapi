package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.Repo
import retrofit2.Call

interface ReposRemoteDataSource {

    fun fetchRepos(ownerLogin: String): Call<List<Repo>>

}