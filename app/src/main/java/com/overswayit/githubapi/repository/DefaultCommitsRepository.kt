package com.overswayit.githubapi.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.overswayit.githubapi.api.CommitsDataSource
import com.overswayit.githubapi.api.CommitsDataSourceFactory
import com.overswayit.githubapi.api.GitHubAPIService
import com.overswayit.githubapi.ui.viewmodel.CommitViewModel
import kotlinx.coroutines.CoroutineScope

class DefaultCommitsRepository(
    private val gitHubAPIService: GitHubAPIService
): CommitsRepository {

    private val pagedListConfig: PagedList.Config = (PagedList.Config.Builder()).setEnablePlaceholders(false).setPageSize(
        CommitsDataSource.PAGE_SIZE).build()

    override fun fetchCommits(ownerLogin: String, repo: String, scope: CoroutineScope): LiveData<PagedList<CommitViewModel>> {
        val commitsDataSourceFactory = CommitsDataSourceFactory(gitHubAPIService, repo, ownerLogin, scope)
        return LivePagedListBuilder(commitsDataSourceFactory, pagedListConfig).build()
    }
}