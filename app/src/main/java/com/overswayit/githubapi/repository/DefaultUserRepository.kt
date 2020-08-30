package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.api.UsersRemoteDataSource
import com.overswayit.githubapi.db.UsersLocalDataSource
import com.overswayit.githubapi.entity.User
import kotlinx.coroutines.*
import retrofit2.Response

class DefaultUserRepository(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val usersLocalDataSource: UsersLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRepository {

    override fun observeUser(login: String): LiveData<User> {
        return usersLocalDataSource.observeUser(login)
    }

    override fun fetchUser(login: String): Response<User> {
        return usersRemoteDataSource.searchUser(login).execute()
    }

    override suspend fun insert(vararg users: User) {
        coroutineScope {
            launch { usersLocalDataSource.insert(*users) }
        }
    }

    override suspend fun delete(vararg users: User) {
        coroutineScope {
            launch { usersLocalDataSource.delete(*users) }
        }
    }

    override suspend fun deleteAll() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { usersLocalDataSource.deleteAll() }
            }
        }
    }
}