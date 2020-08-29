package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

/**
 * Main entry point for accessing users data from db.
 */
interface UsersLocalDataSource {
    suspend fun getUsersByName(name: String): List<User>

    fun observeUsersByName(name: String): LiveData<List<User>>

    suspend fun insertOrReplace(vararg users: User)

    suspend fun insertOrIgnore(vararg users: User)

    suspend fun delete(vararg users: User)

    suspend fun deleteAll()
}