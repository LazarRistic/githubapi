package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

/**
 * Main entry point for accessing users data from db.
 */
interface DefaultUsersLocalDataSource {
    suspend fun getUsersByName(name: String): List<User>

    fun observeUsersByName(name: String): LiveData<List<User>>

    suspend fun insert(vararg users: User)

    suspend fun delete(vararg users: User)

    suspend fun deleteAll()
}