package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

class FakeDefaultUsersLocalDataSource(var users: MutableList<User>? = mutableListOf()) : DefaultUsersLocalDataSource {

    override suspend fun getUsersByName(name: String): List<User> {
        if (users == null || users.isNullOrEmpty()) {
            return emptyList()
        }

        return users!!.toList()
    }

    override fun observeUsersByName(name: String): LiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(vararg users: User) {
        for (user in users) {
            this.users?.add(user)
        }
    }

    override suspend fun delete(vararg users: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        users?.clear()
    }
}