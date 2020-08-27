package com.overswayit.githubapi.api

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

class UsersRemoteDataSource() : DefaultUsersRemoteDataSource {

    override fun observeUsersByName(name: String): LiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override fun searchUsersByName(name: String): List<User> {
        TODO("Not yet implemented")
    }
}