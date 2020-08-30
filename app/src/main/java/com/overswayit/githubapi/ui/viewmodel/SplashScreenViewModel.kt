package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.UsersRepository
import com.overswayit.githubapi.util.NetworkResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val userRepository: UsersRepository
): BasicViewModel() {

    fun fetchInformation(login: String = userLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.fetchUser(login)
            NetworkResponseHandler.userResponseHandler(response, ::insertUser, ::logDebug)
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
    private val usersRepository: UsersRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (SplashScreenViewModel(usersRepository) as T)
}