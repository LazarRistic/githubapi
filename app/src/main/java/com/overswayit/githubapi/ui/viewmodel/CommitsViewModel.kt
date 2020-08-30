package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.overswayit.githubapi.repository.CommitsRepository

class CommitsViewModel(
    commitsRepository: CommitsRepository,
    repoName: String
) : BaseViewModel() {

    private var _commits = MutableLiveData<PagedList<CommitViewModel>>()

    var commits: LiveData<PagedList<CommitViewModel>> = _commits

    init {
        commits = commitsRepository.fetchCommits(userLogin, repoName, ioScope)
    }
}

@Suppress("UNCHECKED_CAST")
class CommitsViewModelFactory(
    private val commitsRepository: CommitsRepository,
    private val repoName: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CommitsViewModel(commitsRepository, repoName) as T)
}