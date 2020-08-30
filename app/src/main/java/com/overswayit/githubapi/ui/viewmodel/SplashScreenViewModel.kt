package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.ReposRepository
import com.overswayit.githubapi.repository.UsersRepository
import com.overswayit.githubapi.util.NetworkResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val userRepository: UsersRepository,
    private val reposRepository: ReposRepository
) : BaseViewModel() {

    fun fetchInformation(login: String = userLogin) {
        fetchUser(login)
        fetchRepos(login)
    }

    private fun fetchRepos(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = reposRepository.fetchRepos(login)
            NetworkResponseHandler.userResponseHandler(response, ::insertRepos, ::logDebug)
        }
    }

    private fun fetchUser(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.fetchUser(login)
            NetworkResponseHandler.userResponseHandler(response, ::insertUser, ::logDebug)
        }
    }

    private fun insertRepos(repos: List<Repo>) {
        repos.forEach {
            viewModelScope.launch(Dispatchers.IO) {
                reposRepository.insert(it)
            }
        }
    }

    private fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insert(user)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SplashScreenViewModelFactory(
    private val usersRepository: UsersRepository,
    private val reposRepository: ReposRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (SplashScreenViewModel(usersRepository, reposRepository) as T)
}