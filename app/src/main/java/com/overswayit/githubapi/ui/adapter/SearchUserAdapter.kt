package com.overswayit.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.overswayit.githubapi.R
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.ui.viewmodel.SearchUsersViewModel

class SearchUserAdapter(private val viewModel: SearchUsersViewModel) : RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    private var users: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_search_users_item, parent, false)
        return SearchUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        holder.setup(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setup(user: User) {
            val textView = itemView.findViewById<TextView>(R.id.name_text_view)
            textView.text = user.name
        }
    }
}