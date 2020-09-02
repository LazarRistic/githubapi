package com.overswayit.githubapi.repository

import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Interface to the data layer.
 */
interface UsersRepository {
    fun observeUser(login: String): Observable<User>

    fun fetchUser(login: String): Single<User>

    fun insert(vararg users: User): Completable

    fun delete(vararg users: User): Completable

    fun deleteAll(): Completable
}