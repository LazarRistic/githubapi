package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.overswayit.githubapi.repository.UsersRepository

class UserDetailsViewModel(
    private val usersRepository: UsersRepository
) : BasicViewModel() {

    fun observeUser(login: String = userLogin) = usersRepository.observeUser(login)

}

@Suppress("UNCHECKED_CAST")
class UserDetailsViewModelFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (UserDetailsViewModel(usersRepository) as T)
}