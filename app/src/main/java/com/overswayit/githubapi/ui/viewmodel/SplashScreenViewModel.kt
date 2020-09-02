package com.overswayit.githubapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overswayit.githubapi.entity.Repo
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.repository.ReposRepository
import com.overswayit.githubapi.repository.UsersRepository
import com.overswayit.githubapi.util.NetworkResponseHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val userRepository: UsersRepository,
    private val reposRepository: ReposRepository
) : BaseViewModel() {

    private val disposable = CompositeDisposable()

    fun fetchInformation(login: String = userLogin) {
        fetchUser(login)
        fetchRepos(login)
    }

    val error: BehaviorSubject<String> = BehaviorSubject.create()

    private fun fetchRepos(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = reposRepository.fetchRepos(login)
            NetworkResponseHandler.userResponseHandler(response, ::insertRepos, ::logDebug)
        }
    }

    private fun fetchUser(login: String) {
        disposable.add(
            userRepository.fetchUser(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    insertUser(it)
                }, {
                    val error = it.message.toString()
                    logDebug(error)
                    this.error.onNext(error)
                })
        )

//        NetworkResponseHandler.userResponseHandler(response, ::insertUser, ::logDebug)

    }

    private fun insertRepos(repos: List<Repo>) {
        repos.forEach {
            viewModelScope.launch(Dispatchers.IO) {
                reposRepository.insert(it)
            }
        }
    }

    private fun insertUser(user: User) {
        disposable.add(
            userRepository.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    logDebug("Insert user: $user succeeded")
                }, { error ->
                    logDebug("$error")
                    this.error.onNext("$error")
                })
        )

    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
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