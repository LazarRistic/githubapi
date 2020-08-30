package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.overswayit.githubapi.api.CommitsDataSourceFactory
import com.overswayit.githubapi.ui.viewmodel.CommitViewModel
import kotlinx.coroutines.CoroutineScope

interface CommitsRepository {
    fun fetchCommits(ownerLogin: String, repo: String, scope: CoroutineScope): LiveData<PagedList<CommitViewModel>>
}