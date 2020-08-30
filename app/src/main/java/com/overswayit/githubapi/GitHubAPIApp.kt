package com.overswayit.githubapi

import android.app.Application
import com.overswayit.githubapi.api.GitHubAPIService
import com.overswayit.githubapi.repository.CommitsRepository
import com.overswayit.githubapi.repository.ReposRepository
import com.overswayit.githubapi.repository.UsersRepository

class GitHubAPIApp : Application() {

    val userRepository: UsersRepository
        get() = ServiceLocator.provideUsersRepository(this)

    val repoRepository: ReposRepository
        get() = ServiceLocator.provideReposRepository(this)

    val commitsRepository: CommitsRepository
        get() = ServiceLocator.provideCommitsRepository()

    val gitHubAPIService: GitHubAPIService
        get() = ServiceLocator.provideGithubAPIService()

    init {
        instance = this
    }

    companion object {
        private var instance: GitHubAPIApp? = null

        fun applicationContext(): GitHubAPIApp {
            return instance as GitHubAPIApp
        }

    }

}