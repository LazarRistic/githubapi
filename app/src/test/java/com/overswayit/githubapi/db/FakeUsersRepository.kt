package com.overswayit.githubapi.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.overswayit.githubapi.api.UserSearchResponse
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.UsersRepository
import retrofit2.Response


/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeUsersRepository : UsersRepository {

    private var usersServicesData: LinkedHashMap<String, User> = LinkedHashMap()

    private val observableUsers = MutableLiveData<List<User>>()

    override fun observeUsersByLogin(login: String): LiveData<List<User>> {
        val userList = ArrayList<User>()
        for (user in usersServicesData.values) {
            if (user.login.contains(login)) {
                userList.add(user)
            }
        }

        observableUsers.postValue(userList)
        return observableUsers
    }

    @Suppress("UNCHECKED_CAST")
    override fun fetchUsers(login: String): Response<UserSearchResponse> {
        val listUsers = ArrayList<User>()

        for (user in usersServicesData) {
            if (user.key.contains(login)) {
                listUsers.add(user.value)
            }
        }

        val body = UserSearchResponse(listUsers.size, listUsers.toList())
        return Response.success(body)
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

    override suspend fun insertOrReplace(vararg users: User) {
        for (user in users) {
            usersServicesData[user.login] = user
        }
    }

    override suspend fun insertOrIgnore(vararg users: User) {
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