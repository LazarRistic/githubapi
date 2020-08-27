package com.overswayit.githubapi

import android.app.Application
import com.overswayit.githubapi.repository.UsersRepository

open class GitHubAPIApp : Application() {

    val userRepository: UsersRepository
        get() = ServiceLocator.provideUsersRepository(this)

}