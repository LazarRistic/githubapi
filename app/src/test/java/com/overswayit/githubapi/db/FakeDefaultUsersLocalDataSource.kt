package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.overswayit.githubapi.entity.User
import kotlinx.coroutines.runBlocking

class FakeDefaultUsersLocalDataSource(private var users: MutableList<User> = mutableListOf()) :
    UsersLocalDataSource {

    private val _query = MutableLiveData("la")

    private val userList: LiveData<User> = _query.map {
        val newUsers: MutableLiveData<User> = MutableLiveData()

        for (user in users) {
            if (user.login == it) {
                newUsers.postValue(user)
                break
            }
        }

        newUsers.value!!
    }

    override fun observeUser(login: String): LiveData<User> {
        runBlocking {
            _query.value = login
        }

        return userList
    }

    override suspend fun insert(vararg users: User) {
        users.forEach { newUser ->
            checkIfExistsAndResponses(newUser, { _, _ -> }, { user -> this.users.add(user) })
        }
    }

    override suspend fun delete(vararg users: User) {
        users.forEach {
            this.users.remove(it)
        }
    }

    override suspend fun deleteAll() {
        users.clear()
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