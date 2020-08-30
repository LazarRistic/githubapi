package com.overswayit.githubapi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.activity.MainActivity
import com.overswayit.githubapi.ui.adapter.CommitsAdapter
import com.overswayit.githubapi.ui.viewmodel.CommitsViewModel
import com.overswayit.githubapi.ui.viewmodel.CommitsViewModelFactory

class CommitsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommitsAdapter

    private val viewModel: CommitsViewModel by viewModels {
        CommitsViewModelFactory(((requireActivity() as MainActivity).applicationContext as GitHubAPIApp).commitsRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)

        arguments?.let {
            val safeArgs = CommitsFragmentArgs.fromBundle(it)
            val repo = safeArgs.repo
            viewModel.fetchCommits(repo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.commits_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = CommitsAdapter()

        val layoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)


        viewModel.commits.observe(requireActivity(), {
            adapter.setUp(it)
        })


    }

}