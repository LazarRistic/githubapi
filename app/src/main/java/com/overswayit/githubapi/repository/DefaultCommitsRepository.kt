package com.overswayit.githubapi.repository

import android.util.Log
import com.overswayit.githubapi.api.GitHubAPIService
import com.overswayit.githubapi.entity.Commit
import com.overswayit.githubapi.service.JsonService
import com.overswayit.githubapi.sharedprefs.SharedPreference

class DefaultCommitsRepository(
    private val gitHubAPIService: GitHubAPIService
): CommitsRepository {

    private val credentials = SharedPreference.getString("CREDENTIALS")

    override fun fetchCommits(ownerLogin: String, repo: String): List<Commit> {
        val commits = ArrayList<Commit>()
        val response = gitHubAPIService.fetchCommits(credentials, ownerLogin, repo).execute()

        if (response.isSuccessful) {
            response.body()?.let {
                for (jsonElement in it) {
                    val jsonObject = JsonService.asJsonObject(jsonElement)
                    commits.add(Commit(jsonObject))
                }
            }
        }

        response.errorBody()?.string()?.let { Log.d("CommitsRepository", it) }

        return commits
    }
}