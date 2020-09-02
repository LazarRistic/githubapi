package com.overswayit.githubapi.repository

import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject


/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeUsersRepository : UsersRepository {

    private var usersServicesData: LinkedHashMap<String, User> = LinkedHashMap()

    override fun observeUser(login: String): Observable<User> {
        var fetchedUser: User? = null

        for (user in usersServicesData.values) {
            if (user.login == login) {
                fetchedUser = user
                break
            }
        }

        return if (fetchedUser == null) {
            Observable.empty()
        } else{
            Observable.just(fetchedUser)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun fetchUser(login: String): Single<User> {
        var fetchedUser: User? = null

        for (user in usersServicesData) {
            if (user.key == login) {
                fetchedUser = user.value
                break
            }
        }

        return Single.just(fetchedUser)
    }

    override fun insert(vararg users: User): Completable {
        var completed = true

        for (user in users) {
            if (!usersServicesData.containsKey(user.login)) {
                usersServicesData[user.login] = user
            }

            if (usersServicesData[user.login] != user) {
                completed = false
            }
        }

        return if (completed) {
            Completable.complete()
        } else {
            Completable.never()
        }
    }

    override fun delete(vararg users: User): Completable {
        var completed = true

        for (user in users) {
            usersServicesData.remove(user.name)

            if (usersServicesData.containsKey(user.name)) {
                completed = false
            }
        }

        return if (completed) {
            Completable.complete()
        } else {
            Completable.never()
        }
    }

    override fun deleteAll(): Completable {
        usersServicesData.clear()

        return if (usersServicesData.size > 0) {
            Completable.never()
        } else {
            Completable.complete()
        }
    }

    fun addUser(user: User) {
        usersServicesData[user.login] = user
    }
}