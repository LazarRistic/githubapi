package com.overswayit.githubapi.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.adapter.SearchUserAdapter
import com.overswayit.githubapi.ui.viewmodel.SearchUsersViewModel
import com.overswayit.githubapi.ui.viewmodel.SearchUsersViewModelFactory


class SearchUsersActivity : AppCompatActivity() {

    private lateinit var adapter: SearchUserAdapter

    private val viewModel by viewModels<SearchUsersViewModel> {
        SearchUsersViewModelFactory((applicationContext as GitHubAPIApp).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)

        adapter = SearchUserAdapter()

        val layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)

        viewModel.observeUsersByName().observe(this, {
            adapter.setUsers(it)
        })

        viewModel.setQuery("laza")
    }
}