package com.overswayit.githubapi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.activity.MainActivity
import com.overswayit.githubapi.ui.view.AccInfoItemView
import com.overswayit.githubapi.ui.view.UserProfileView
import com.overswayit.githubapi.ui.viewmodel.UserDetailsViewModel
import com.overswayit.githubapi.ui.viewmodel.UserDetailsViewModelFactory

class UserDetailsFragment : Fragment() {

    private val viewModel: UserDetailsViewModel by viewModels {
        UserDetailsViewModelFactory((((requireActivity() as MainActivity).applicationContext) as GitHubAPIApp).userRepository)
    }

    private lateinit var userProfileView: UserProfileView
    private lateinit var nameAccInfoItemView: AccInfoItemView
    private lateinit var emailAccInfoItemView: AccInfoItemView
    private lateinit var blogAccInfoItemView: AccInfoItemView
    private lateinit var locationAccInfoItemView: AccInfoItemView
    private lateinit var followersTextView: TextView
    private lateinit var followingTextView: TextView
    private lateinit var reposTextView: TextView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileView = view.findViewById(R.id.user_profile_view)
        nameAccInfoItemView = view.findViewById(R.id.name_acc_info_item_view)
        emailAccInfoItemView = view.findViewById(R.id.email_acc_info_item_view)
        blogAccInfoItemView = view.findViewById(R.id.blog_acc_info_item_view)
        locationAccInfoItemView = view.findViewById(R.id.location_acc_info_item_view)
        followersTextView = view.findViewById(R.id.followers_text_view)
        followingTextView = view.findViewById(R.id.following_text_View)
        reposTextView = view.findViewById(R.id.public_repos_text_view)
        navController = findNavController()

        viewModel.observeUser().observe(requireActivity(), {user ->
            userProfileView.setUser(user)

            user.name?.let {
                nameAccInfoItemView.text = it
            }

            user.email?.let {
                emailAccInfoItemView.text = it
            }

            user.blog?.let {
                blogAccInfoItemView.text = it
            }

            user.location?.let {
                locationAccInfoItemView.text = it
            }

            followersTextView.text = "${user.followers ?: 0}"
            followingTextView.text = "${user.following ?: 0}"
            reposTextView.text = "${user.repos ?: 0}"
        })

        userProfileView.setOnReposClickListener{
            navController.navigate(R.id.action_userDetailsFragment_to_reposFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nameAccInfoItemView.text = "NEKO IME"
        emailAccInfoItemView.text = "NEKO EMAIL"
        blogAccInfoItemView.text = "NEKO PHONE"
        locationAccInfoItemView.text = "NEKO ADDRESS"
    }

}