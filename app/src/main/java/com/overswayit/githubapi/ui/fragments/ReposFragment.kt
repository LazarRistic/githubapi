package com.overswayit.githubapi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.activity.MainActivity
import com.overswayit.githubapi.ui.adapter.ReposAdapter
import com.overswayit.githubapi.ui.viewmodel.ReposViewModel
import com.overswayit.githubapi.ui.viewmodel.ReposViewModelFactory

class ReposFragment : Fragment() {

    private lateinit var adapter: ReposAdapter
    private lateinit var recyclerView: RecyclerView

    private val viewModel: ReposViewModel by viewModels {
        ReposViewModelFactory((((requireActivity() as MainActivity).applicationContext) as GitHubAPIApp).repoRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ReposAdapter()

        val layoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.observeRepos().observe(requireActivity(), {
            adapter.setRepos(it)
        })
    }

}