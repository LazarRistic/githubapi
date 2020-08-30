package com.overswayit.githubapi.api

import com.overswayit.githubapi.entity.Repo
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Response

class FakeDefaultReposRemoteDataSource(private val repos: ArrayList<Repo> = ArrayList()) : ReposRemoteDataSource {

    private val fakeCredentials = "Basic FAKE_CREDENTIALS"

    override fun fetchRepos(ownerLogin: String): Call<List<Repo>> {
        val repos = ArrayList<Repo>()

        for (repo in this.repos) {
            if (repo.owner.login == ownerLogin) {
                repos.add(repo)
            }
        }

        val service: GitHubAPIService = Mockito.mock(GitHubAPIService::class.java)
        val call = createSuccessfulCall(repos.toList())
        Mockito.`when`(service.fetchRepos(fakeCredentials, ownerLogin)).thenReturn(call)

        return service.fetchRepos(fakeCredentials, ownerLogin)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> createSuccessfulCall(body: T): Call<T> {
        val success = Response.success(body)
        val call = Mockito.mock(Call::class.java) as Call<T>
        Mockito.`when`(call.execute()).thenReturn(success)

        return call
    }

}