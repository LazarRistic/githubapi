package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.UsersRepository
import retrofit2.Response


/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeUsersRepository : UsersRepository {

    private var usersServicesData: LinkedHashMap<String, User> = LinkedHashMap()

    private val observableUser = MutableLiveData<User>()

    override fun observeUser(login: String): LiveData<User> {
        var fetchedUser: User? = null

        for (user in usersServicesData.values) {
            if (user.login == login) {
                fetchedUser = user
            }
        }

        fetchedUser?.let {

        }

        if (fetchedUser == null) {
            observableUser.postValue(null)
        } else {
            observableUser.postValue(fetchedUser)
        }

        return observableUser
    }

    @Suppress("UNCHECKED_CAST")
    override fun fetchUser(login: String): Response<User> {
        var fetchedUser: User? = null

        for (user in usersServicesData) {
            if (user.key == login) {
                fetchedUser = user.value
                break
            }
        }

        return Response.success(fetchedUser)
    }

    override suspend fun insert(vararg users: User) {
        for (user in users) {
            if (!usersServicesData.containsKey(user.login)) {
                usersServicesData[user.login] = user
            }
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
        usersServicesData[user.login] = user
    }
}