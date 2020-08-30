package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.User
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response

class FakeDefaultRemoteDataSource(private val users: ArrayList<User> = ArrayList()) :
    UsersRemoteDataSource {

    private val fakeCredentials = "Basic FAKE_CREDENTIALS"

    @Suppress("UNCHECKED_CAST")
    override fun searchUser(login: String): Call<User> {
        val service: GitHubAPIService = mock(GitHubAPIService::class.java)

        for (user in users) {
            if (user.login == login) {
                val call = createSuccessfulCall(user)
                Mockito.`when`(service.searchUser(fakeCredentials, login)).thenReturn(call)
                break
            }
        }

        return service.searchUser(fakeCredentials, login)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> createSuccessfulCall(body: T): Call<T> {
        val success = Response.success(body)
        val call = mock(Call::class.java) as Call<T>
        Mockito.`when`(call.execute()).thenReturn(success)

        return call
    }

}