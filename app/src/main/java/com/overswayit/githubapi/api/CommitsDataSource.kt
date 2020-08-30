package com.overswayit.githubapi.api

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.google.gson.JsonArray
import com.overswayit.githubapi.entity.Commit
import com.overswayit.githubapi.service.JsonService
import com.overswayit.githubapi.sharedprefs.SharedPreference
import com.overswayit.githubapi.ui.viewmodel.CommitViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class CommitsDataSource(
    private val gitHubAPIService: GitHubAPIService,
    private val repo: String,
    private val ownerLogin: String,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, CommitViewModel>() {

    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 20
    }

    //we will start from the first page which is 1
    @Suppress("PrivatePropertyName")
    private val FIRST_PAGE = 1

    private val credentials = SharedPreference.getString("CREDENTIALS")

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CommitViewModel>
    ) {
        scope.launch(Dispatchers.IO) {
            val response = gitHubAPIService.fetchCommits(
                credentials,
                perPage = "$PAGE_SIZE",
                page = "$FIRST_PAGE",
                repo = repo,
                login = ownerLogin
            ).execute()



            val viewModels = parseResponse(response)

            callback.onResult(viewModels, null, FIRST_PAGE + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CommitViewModel>) {
        scope.launch(Dispatchers.IO) {

            val response = gitHubAPIService.fetchCommits(
                credentials,
                perPage = "$PAGE_SIZE",
                page = "${params.key}",
                repo = repo,
                login = ownerLogin
            ).execute()

            //if the current page is greater than one
            //we are decrementing the page number
            //else there is no previous page
            val adjacentKey: Int? = if (params.key > 1) params.key - 1 else null

            if (response.body() != null) {
                //passing the loaded data
                //and the previous page key
                val viewModels = parseResponse(response)
                callback.onResult(viewModels, adjacentKey)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CommitViewModel>) {
        scope.launch(Dispatchers.IO) {
            val response = gitHubAPIService.fetchCommits(
                credentials,
                perPage = "$PAGE_SIZE",
                page = "${params.key}",
                repo = repo,
                login = ownerLogin
            ).execute()

            if (response.body() != null) {
                val viewModels = parseResponse(response)

                //if the response has next page
                //incrementing the next page number
                val key: Int? = if (viewModels.isNotEmpty()) params.key + 1 else null

                //passing the loaded data and next page value
                callback.onResult(viewModels, key)
            }
        }

    }

    private fun parseResponse(response: Response<JsonArray>): MutableList<CommitViewModel> {
        val viewModels: MutableList<CommitViewModel> = mutableListOf()

        if (response.isSuccessful) {
            response.body()?.let {
                for (jsonElement in it) {
                    val jsonObject = JsonService.asJsonObject(jsonElement)
                    val viewModel = CommitViewModel(Commit(jsonObject))
                    viewModels.add(viewModel)
                }
            }
        }

        response.errorBody()?.string()?.let { Log.d("CommitsRepository", it) }

        return viewModels
    }

}