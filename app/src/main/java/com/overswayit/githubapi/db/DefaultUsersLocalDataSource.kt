package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class DefaultUsersLocalDataSource internal constructor(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersLocalDataSource {

    override fun observeUser(login: String): LiveData<User> = userDao.observeUser(login)

    override suspend fun insert(vararg users: User) = withContext(ioDispatcher) {
        userDao.insert(*users)
    }

    override suspend fun delete(vararg users: User) = withContext(ioDispatcher) {
        userDao.delete(*users)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        userDao.deleteAll()
    }

}