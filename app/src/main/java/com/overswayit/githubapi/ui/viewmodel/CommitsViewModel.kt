package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.*
import com.overswayit.githubapi.repository.CommitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommitsViewModel(
    private val commitsRepository: CommitsRepository
) : BaseViewModel() {

    private val _commits = MutableLiveData<List<CommitViewModel>>()

    val commits: LiveData<List<CommitViewModel>> = _commits

    fun fetchCommits(repoName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val commits = commitsRepository.fetchCommits(userLogin, repoName)

            val viewModels = ArrayList<CommitViewModel>()

            commits.forEach {
                val viewModel = CommitViewModel(it)
                viewModels.add(viewModel)
            }

            _commits.postValue(viewModels)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CommitsViewModelFactory(
    private val commitsRepository: CommitsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CommitsViewModel(commitsRepository) as T)
}