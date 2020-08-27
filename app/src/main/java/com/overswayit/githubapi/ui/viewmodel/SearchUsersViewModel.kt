package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overswayit.githubapi.repository.UsersRepository
import com.overswayit.githubapi.entity.User
import kotlinx.coroutines.launch

/**
 * ViewModel for the searching users screen.
 */
class SearchUsersViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    fun observeUsersByName(name: String): LiveData<List<User>> {
        return usersRepository.observeUsersByName(name)
    }

    suspend fun getUsersByName(name: String): List<User> {
        return usersRepository.getUsersByName(name)
    }

    fun insert(vararg users: User) {
        viewModelScope.launch {
            usersRepository.insert(*users)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SearchUsersViewModelFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (SearchUsersViewModel(usersRepository) as T)
}