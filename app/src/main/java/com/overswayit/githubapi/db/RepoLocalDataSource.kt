package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.Repo

/**
 * Main entry point for accessing repos data from db.
 */
interface RepoLocalDataSource {

    fun observeRepo(ownerLogin: String, name: String): LiveData<Repo>

    fun observeRepos(ownerLogin: String): LiveData<List<Repo>>

    suspend fun insert(vararg repos: Repo)

    suspend fun deleteAll()
}