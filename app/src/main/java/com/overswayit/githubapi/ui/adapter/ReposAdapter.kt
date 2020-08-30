package com.overswayit.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.R
import com.overswayit.githubapi.entity.Repo

class ReposAdapter: RecyclerView.Adapter<ReposAdapter.ReposAdapterViewHolder>() {

    private var repos: List<Repo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_repos_item, parent, false)
        return ReposAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReposAdapterViewHolder, position: Int) {
        holder.setUp(repos[position])
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    fun setRepos(repos: List<Repo>) {
        this.repos = repos
        notifyDataSetChanged()
    }

    class ReposAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        lateinit var titleTextView: TextView
        lateinit var descriptionTextView: TextView
        lateinit var openIssuesTextView: TextView
        lateinit var starsTextView: TextView
        lateinit var forksTextView: TextView
        lateinit var watchersTextView: TextView

        fun setUp(repo: Repo) {
            findViews(repo)
            defaultState()
            fillViews(repo)
        }

        private fun fillViews(repo: Repo) {
            titleTextView.text = repo.name
            descriptionTextView.text = repo.description
            openIssuesTextView.text = "${repo.openIssues}"
            starsTextView.text = "${repo.stars}"
            forksTextView.text = "${repo.forks}"
            watchersTextView.text = "${repo.watchers}"
        }

        private fun defaultState() {
            titleTextView.text = ""
            descriptionTextView.text = ""
            openIssuesTextView.text = "0"
            starsTextView.text = "0"
            forksTextView.text = "0"
            watchersTextView.text = "0"
        }

        private fun findViews(repo: Repo) {
            titleTextView = itemView.findViewById(R.id.title_text_view)
            descriptionTextView = itemView.findViewById(R.id.description_text_view)
            openIssuesTextView = itemView.findViewById(R.id.open_issues_text_view)
            starsTextView = itemView.findViewById(R.id.stars_text_view)
            forksTextView = itemView.findViewById(R.id.fork_text_view)
            watchersTextView = itemView.findViewById(R.id.watcher_text_view)
        }
    }
}