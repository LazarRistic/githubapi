package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User
import retrofit2.Response

/**
 * Interface to the data layer.
 */
interface UsersRepository {
    fun observeUser(login: String): LiveData<User>

    fun fetchUser(login: String): Response<User>

    suspend fun insert(vararg users: User)

    suspend fun delete(vararg users: User)

    suspend fun deleteAll()
}