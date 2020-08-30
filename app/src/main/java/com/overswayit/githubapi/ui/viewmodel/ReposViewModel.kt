package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.repository.ReposRepository
import com.overswayit.githubapi.util.NetworkResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReposViewModel(
    private val reposRepository: ReposRepository
) : BaseViewModel() {

    fun observeRepos(ownerLogin: String = userLogin): LiveData<List<Repo>> {
        fetchRepos(ownerLogin)

        return reposRepository.observeRepos(ownerLogin)
    }

    private fun fetchRepos(ownerLogin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = reposRepository.fetchRepos(ownerLogin)
            NetworkResponseHandler.userResponseHandler(response, ::successfulFetch, ::logDebug)
        }
    }

    private fun successfulFetch(list: List<Repo>) {
        viewModelScope.launch(Dispatchers.IO) {
            list.forEach {
                reposRepository.insert(it)
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ReposViewModelFactory(
    private val reposRepository: ReposRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (ReposViewModel(reposRepository) as T)
}