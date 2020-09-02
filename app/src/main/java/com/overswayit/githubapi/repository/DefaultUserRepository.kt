package com.overswayit.githubapi.repository

import com.overswayit.githubapi.api.UsersRemoteDataSource
import com.overswayit.githubapi.db.UsersLocalDataSource
import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

class DefaultUserRepository(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val usersLocalDataSource: UsersLocalDataSource
) : UsersRepository {

    override fun observeUser(login: String): Observable<User> {
        return usersLocalDataSource.observeUser(login)
    }

    override fun fetchUser(login: String): Single<User> {
        return usersRemoteDataSource.searchUser(login)
    }

    override fun insert(vararg users: User): Completable {
        return usersLocalDataSource.insert(*users)
    }

    override fun delete(vararg users: User): Completable {
        return usersLocalDataSource.delete(*users)
    }

    override fun deleteAll(): Completable {
        return usersLocalDataSource.deleteAll()
    }
}