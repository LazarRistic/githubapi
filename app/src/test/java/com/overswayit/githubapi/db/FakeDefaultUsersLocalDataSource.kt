package com.overswayit.githubapi.db

import com.overswayit.githubapi.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class FakeDefaultUsersLocalDataSource(private var users: MutableList<User> = mutableListOf()) :
    UsersLocalDataSource {

    private val _query: BehaviorSubject<String> = BehaviorSubject.create()

    private val userList: Observable<User> = _query.map {
        val newUsers: BehaviorSubject<User> = BehaviorSubject.create()

        for (user in users) {
            if (user.login == it) {
                newUsers.onNext(user)
                break
            }
        }

        newUsers.value!!
    }

    override fun observeUser(login: String): Observable<User> {
        _query.onNext(login)

        return userList
    }

    override fun insert(vararg users: User): Completable {
        users.forEach { newUser ->
            checkIfExistsAndResponses(newUser, { _, _ -> }, { user -> this.users.add(user) })
        }

        return Completable.complete()
    }

    override fun delete(vararg users: User): Completable {
        var completed = true

        users.forEach {
            this.users.remove(it)

            if (this.users.contains(it)){
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
        users.clear()

        return if (users.size > 0) {
            Completable.complete()
        } else {
            Completable.never()
        }
    }

    /**
     * Check if [newUser] exists and if exists do fun [exists] and if not do fun [missing]
     *
     * @param [newUser] check if exists in global MutableLists [users]
     * @param [exists] if [User] exists in global MutableLists [users] then do fun [exists]
     * @param [missing] if [User] does not exists in global MutableLists [users] then do fun [missing]
     */
    private fun checkIfExistsAndResponses(
        newUser: User,
        exists: (index: Int, user: User) -> Unit,
        missing: (user: User) -> Unit
    ) {
        var inserted = false
        for (i in 0 until users.size) {
            if (users[i].id == newUser.id) {
                exists(i, newUser)
                inserted = true
                break
            }
        }

        if (!inserted) {
            missing(newUser)
        }
    }
}