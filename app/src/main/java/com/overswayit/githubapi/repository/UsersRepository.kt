package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

/**
 * Interface to the data layer.
 */
interface UsersRepository {
    fun observeUsersByName(name: String): LiveData<List<User>>

    suspend fun getUsersByName(name: String): List<User>

    suspend fun insert(vararg users: User)

    suspend fun delete(vararg users: User)

    suspend fun deleteAll()
}