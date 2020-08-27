package com.overswayit.githubapi.api

import androidx.lifecycle.LiveData
import com.overswayit.githubapi.entity.User

interface DefaultUsersRemoteDataSource {

    fun observeUsersByName(name: String): LiveData<List<User>>

    fun searchUsersByName(name: String): List<User>

}