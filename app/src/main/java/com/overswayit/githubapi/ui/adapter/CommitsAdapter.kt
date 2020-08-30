package com.overswayit.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.viewmodel.CommitViewModel

private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<CommitViewModel>() {
    override fun areItemsTheSame(oldItem: CommitViewModel, newItem: CommitViewModel): Boolean {
        return oldItem.sha() == newItem.sha()
    }

    override fun areContentsTheSame(oldItem: CommitViewModel, newItem: CommitViewModel): Boolean {
        return oldItem.sha() == newItem.sha()
    }
}

class CommitsAdapter :
    PagedListAdapter<CommitViewModel, CommitsAdapter.CommitsAdapterViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitsAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_commit_item, parent, false)

        return CommitsAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommitsAdapterViewHolder, position: Int) {
        val viewModel = getItem(position)
        holder.setUp(viewModel)
    }

    fun setUp(viewModels: PagedList<CommitViewModel>?) {
        if (viewModels != null)
        submitList(viewModels)
    }

    class CommitsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var messageTextView: TextView
        private lateinit var dateTextView: TextView
        private lateinit var authorImageView: ImageView
        private lateinit var authorTextView: TextView
        private lateinit var shaTextView: TextView
        private lateinit var parentsTextView: TextView

        fun setUp(viewModel: CommitViewModel?) {
            findViews()
            fillViews(viewModel)
        }

        private fun fillViews(viewModel: CommitViewModel?) {
            viewModel?.let {
                messageTextView.text = viewModel.message()
                dateTextView.text = viewModel.date(itemView.context)
                authorTextView.text = viewModel.author()
                shaTextView.text = viewModel.sha()
                parentsTextView.text = viewModel.parentsSha()
                viewModel.authorImage(itemView.context, authorImageView)
            }
        }

        private fun findViews() {
            messageTextView = itemView.findViewById(R.id.message_text_view)
            dateTextView = itemView.findViewById(R.id.date_text_view)
            authorImageView = itemView.findViewById(R.id.avatar_url_image_view)
            authorTextView = itemView.findViewById(R.id.authors_text_view)
            shaTextView = itemView.findViewById(R.id.sha_text_view)
            parentsTextView = itemView.findViewById(R.id.parents_text_view)
        }
    }
}