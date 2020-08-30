package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.Repo
import retrofit2.Response

/**
 * Interface to the data layer.
 */
interface ReposRepository {

    fun observeRepo(ownerLogin: String, name: String): LiveData<Repo>

    fun observeRepos(ownerLogin: String): LiveData<List<Repo>>

    fun fetchRepos(ownerLogin: String): Response<List<Repo>>

    suspend fun insert(repo: Repo)

    suspend fun deleteAll()
}