package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

/**
 * Main entry point for accessing users data from db.
 */
interface UsersLocalDataSource {

    fun observeUser(login: String): LiveData<User>

    suspend fun insert(vararg users: User)

    suspend fun delete(vararg users: User)

    suspend fun deleteAll()
}