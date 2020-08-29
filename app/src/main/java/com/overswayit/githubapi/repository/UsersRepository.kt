package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.api.UserSearchResponse
import com.overswayit.githubapi.entity.User
import retrofit2.Response

/**
 * Interface to the data layer.
 */
interface UsersRepository {
    fun observeUsersByLogin(login: String): LiveData<List<User>>

    fun fetchUsers(login: String): Response<UserSearchResponse>

    fun fetchUser(login: String): Response<User>

    suspend fun insertOrReplace(vararg users: User)

    suspend fun insertOrIgnore(vararg users: User)

    suspend fun delete(vararg users: User)

    suspend fun deleteAll()
}