package com.overswayit.githubapi.ui.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.overswayit.githubapi.R
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.util.MetricsUtil

class SearchUserAdapter :
    RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    private var users: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_search_users_item, parent, false)
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

        private lateinit var nameTextView: TextView
        private lateinit var loginTextView: TextView
        private lateinit var avatarImageView: ImageView
        private lateinit var bioTextView: TextView
        private lateinit var locationTextView: TextView

        fun setup(user: User) {
            findViews()
            defaultState()

            loginTextView.text = user.login

            if (!TextUtils.isEmpty(user.name)) {
                nameTextView.text = user.name
                nameTextView.visibility = View.VISIBLE
            }

            if (!TextUtils.isEmpty(user.bio)) {
                bioTextView.text = user.bio
                bioTextView.visibility = View.VISIBLE
            }

            if (!TextUtils.isEmpty(user.location)) {
                locationTextView.text = user.location
                locationTextView.visibility = View.VISIBLE
            }

            val avatarSize = MetricsUtil.convertDpToPixel(48F, itemView.context)
            val avatarUrl = user.avatarUri
                ?: "http://www.gravatar.com/avatar/${user.login}?s=$avatarSize&d=identicon&r=P"

            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(itemView).load(user.avatarUri).circleCrop().into(avatarImageView)
            }
        }

        private fun defaultState() {
            nameTextView.visibility = View.GONE
            bioTextView.visibility = View.GONE
            locationTextView.visibility = View.GONE

            nameTextView.text = ""
            bioTextView.text = ""
            locationTextView.text = ""
        }

        private fun findViews() {
            nameTextView = itemView.findViewById(R.id.name_text_view)
            loginTextView = itemView.findViewById(R.id.login_text_view)
            avatarImageView = itemView.findViewById(R.id.avatar_image_view)
            bioTextView = itemView.findViewById(R.id.bio_text_view)
            locationTextView = itemView.findViewById(R.id.location_text_view)
        }
    }
}