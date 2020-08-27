package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.UsersRepository

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeUsersRepository : UsersRepository {

    var usersServicesData: LinkedHashMap<String, User> = LinkedHashMap()

    private val observableUsers = MutableLiveData<List<User>>()

    override suspend fun getUsersByName(name: String): List<User> {
        val users: LinkedHashMap<String, User> = LinkedHashMap()

        for (user in usersServicesData) {
            if (user.key.contains(name)) {
                users[user.key] = user.value
            }
        }

        return users.values.toList()
    }

    override fun observeUsersByName(name: String): LiveData<List<User>> {
        val userList = ArrayList<User>()
        for (user in usersServicesData.values) {
            if (user.name.contains(name)) {
                userList.add(user)
            }
        }

        observableUsers.postValue(userList)
        return observableUsers
    }

    override suspend fun insert(vararg users: User) {
        for (user in users) {
            usersServicesData[user.name] = user
        }
    }

    override suspend fun delete(vararg users: User) {
        for (user in users) {
            usersServicesData.remove(user.name)
        }
    }

    override suspend fun deleteAll() {
        usersServicesData.clear()
    }

    fun addUser(user: User) {
        usersServicesData[user.name] = user
    }
}