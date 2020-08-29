package com.overswayit.githubapi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.overswayit.githubapi.api.UserSearchResponse
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.UsersRepository
import com.overswayit.githubapi.util.NetworkResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for the searching users screen.
 */
class SearchUsersViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {
    @Suppress("PrivatePropertyName")
    private val LOG_VIEW_MODEL = false

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "SearchUsersViewModel"


    private val _query = MutableLiveData<String>()

    val users: LiveData<List<User>> = _query.switchMap {
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.deleteAll()
            val fetchUsersResponse = usersRepository.fetchUsers(it)
            NetworkResponseHandler.userResponseHandler(fetchUsersResponse, ::successfulFetchUsers, ::logDebug)
        }

        usersRepository.observeUsersByLogin(it)
    }

    fun observeUsersByName(): LiveData<List<User>> {
        return users
    }

    fun setQuery(name: String) {
        _query.value = name
    }

    private fun successfulFetchUsers(usersSearchResponse: UserSearchResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            for (user in usersSearchResponse.items) {
                val fetchUserResponse = usersRepository.fetchUser(user.login)
                usersRepository.insertOrIgnore(user)
                NetworkResponseHandler.userResponseHandler(fetchUserResponse, ::insertSingle, ::logDebug)
            }
        }
    }

    private fun insertSingle(user: User) {
        insert(user)
    }

    private fun insert(vararg users: User) {
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.insertOrReplace(*users)
        }
    }

    private fun logDebug(error: String) {
        if (LOG_VIEW_MODEL) {
            Log.d(LOG_TAG, error)
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