package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.api.DefaultUsersRemoteDataSource
import com.overswayit.githubapi.db.DefaultUsersLocalDataSource
import com.overswayit.githubapi.entity.User
import kotlinx.coroutines.*

class DefaultUserRepository(
    private val usersRemoteDataSource: DefaultUsersRemoteDataSource,
    private val usersLocalDataSource: DefaultUsersLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRepository {

    override fun observeUsersByName(name: String): LiveData<List<User>> {
        return usersLocalDataSource.observeUsersByName(name)
    }

    override suspend fun getUsersByName(name: String): List<User> {
//        TODO: updateUsersFromRemoteDataSource(name)
        return usersLocalDataSource.getUsersByName(name)
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

    private suspend fun updateUsersFromRemoteDataSource(name: String) {
//        TODO: Code below
//        val remoteUsers = usersRemoteDataSource.getUsersByName(name)
//        usersLocalDataSource.deleteAll()
//        remoteUsers.forEAch { user ->
//          usersLocalDataSource.insert(user)
//        }
    }

}