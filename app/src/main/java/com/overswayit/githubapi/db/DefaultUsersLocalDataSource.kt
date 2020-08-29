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

    override suspend fun getUsersByName(name: String): List<User> = userDao.getUsersByName(name)

    override fun observeUsersByName(name: String): LiveData<List<User>> =  userDao.observeUsersByName("%$name%")

    override suspend fun insertOrReplace(vararg users: User) = withContext(ioDispatcher) {
        userDao.insertOrReplace(*users)
    }

    override suspend fun insertOrIgnore(vararg users: User) {
        userDao.insertOrIgnore(*users)
    }

    override suspend fun delete(vararg users: User) = withContext(ioDispatcher) {
        userDao.delete(*users)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        userDao.deleteAll()
    }

}