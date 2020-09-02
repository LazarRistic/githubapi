package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import io.reactivex.Single
import retrofit2.Call

interface UsersRemoteDataSource {

    fun searchUser(login: String): Single<User>

}