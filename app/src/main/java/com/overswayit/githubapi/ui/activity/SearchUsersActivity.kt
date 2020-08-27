package com.overswayit.githubapi.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.ui.adapter.SearchUserAdapter
import com.overswayit.githubapi.ui.viewmodel.SearchUsersViewModel
import com.overswayit.githubapi.ui.viewmodel.SearchUsersViewModelFactory

class SearchUsersActivity : AppCompatActivity() {

    private lateinit var adapter: SearchUserAdapter

    private val viewModel by viewModels<SearchUsersViewModel>() {
        SearchUsersViewModelFactory((applicationContext as GitHubAPIApp).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_users)

        adapter = SearchUserAdapter(viewModel)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.observeUsersByName("%laza%").observe(this, Observer {
            adapter.setUsers(it)
        })

        val user1 = User(1, "laza", "laza", "laza", "laza", "laza", "laza", "laza", "laza")
        val user2 = User(2, "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2", "laza2")
        val user3 = User(3, "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3", "laza3")
        viewModel.insert(user1, user2)
    }
}