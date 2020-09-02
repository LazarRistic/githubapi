package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import io.reactivex.Single
import org.mockito.Mockito
import org.mockito.Mockito.mock

class FakeDefaultUsersRemoteDataSource(private val users: ArrayList<User> = ArrayList()) :
    UsersRemoteDataSource {

    override fun searchUser(login: String): Single<User> {
        val service: GitHubAPIService = mock(GitHubAPIService::class.java)

        for (user in users) {
            if (user.login == login) {
                Mockito.`when`(service.searchUser(login)).thenReturn(Single.just(user))
                break
            }
        }

        return service.searchUser(login)
    }
}