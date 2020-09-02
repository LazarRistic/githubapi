package com.overswayit.githubapi.db

import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Main entry point for accessing users data from db.
 */
interface UsersLocalDataSource {

    fun observeUser(login: String): Observable<User>

    fun insert(vararg users: User): Completable

    fun delete(vararg users: User): Completable

    fun deleteAll(): Completable
}