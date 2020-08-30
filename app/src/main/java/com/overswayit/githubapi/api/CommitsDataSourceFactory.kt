package com.overswayit.githubapi.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.overswayit.githubapi.ui.viewmodel.CommitViewModel
import kotlinx.coroutines.CoroutineScope

class CommitsDataSourceFactory(
    private val gitHubAPIService: GitHubAPIService,
    private val repo: String,
    private val ownerLogin: String,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, CommitViewModel>() {

    private val commitLiveViewModel: MutableLiveData<PageKeyedDataSource<Int, CommitViewModel>> = MutableLiveData()

    override fun create(): DataSource<Int, CommitViewModel> {
        //getting our data source object
        val commitsDataSource = CommitsDataSource(gitHubAPIService, repo, ownerLogin, scope)

        //posting the datasource to get the values
        commitLiveViewModel.postValue(commitsDataSource)

        //returning the datasource
        return commitsDataSource
    }



}