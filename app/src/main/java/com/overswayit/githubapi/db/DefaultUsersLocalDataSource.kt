package com.overswayit.githubapi.db

import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class DefaultUsersLocalDataSource internal constructor(
    private val userDao: UserDao
) : UsersLocalDataSource {

    override fun observeUser(login: String): Observable<User> = userDao.observeUser(login)

    override fun insert(vararg users: User): Completable {
        return userDao.insert(*users)
    }

    override fun delete(vararg users: User): Completable {
        return userDao.delete(*users)
    }

    override fun deleteAll(): Completable {
        return userDao.deleteAll()
    }

}