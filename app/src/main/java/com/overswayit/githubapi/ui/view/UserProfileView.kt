package com.overswayit.githubapi.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.overswayit.githubapi.R
import com.overswayit.githubapi.entity.User
import com.overswayit.githubapi.util.MetricsUtil

class UserProfileView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private lateinit var loginTextView: TextView
    private lateinit var companyTextView: TextView
    private lateinit var bioTextView: TextView
    private lateinit var avatarImageView: ImageView
    private lateinit var repoHolderView: View

    private val avatarSize: Float
    private lateinit var loginText: String
    private lateinit var companyText: String
    private lateinit var bioText: String
    private lateinit var nameText: String
    private lateinit var avatarUrl: String

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.UserProfileView, 0, 0
        )

        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_user_profile, this, true)

        avatarSize = MetricsUtil.convertDpToPixel(48F, context)
        findViews()
        getValues(typedArray)

        typedArray.recycle()

        repoHolderView.setOnClickListener {
            Toast.makeText(context, "Repo button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUser(user: User) {
        setAvatarUrl(user.url)
        setLogin(user.login)

        user.company?.let {
            setCompany(it)
        }

        user.bio?.let {
            setBio(it)
        }
        user.name?.let {
            setName(it)
        }
    }

    private fun setAvatarUrl(url: String) {
        avatarUrl = url

        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(context).load(avatarUrl).circleCrop().into(object: CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    avatarImageView.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    avatarImageView.setImageDrawable(placeholder)
                }

            })
        }
    }

    private fun setLogin(login: String) {
        loginText = login
        loginTextView.text = loginText
    }

    private fun setCompany(company: String) {
        companyText = company
        companyTextView.text = companyText
    }

    private fun setBio(bio: String) {
        bioText = bio
        bioTextView.text = bioText

        if (TextUtils.isEmpty(bio)) {
            bioTextView.visibility = GONE
        } else {
            bioTextView.visibility = VISIBLE
        }
    }

    private fun setName(name: String) {
        nameText = name
    }

    private fun getValues(typedArray: TypedArray) {
        setLogin(typedArray.getString(R.styleable.UserProfileView_login) ?: "")
        setCompany(typedArray.getString(R.styleable.UserProfileView_company) ?: "")
        setBio(typedArray.getString(R.styleable.UserProfileView_bio) ?: "")
        setName(typedArray.getString(R.styleable.UserProfileView_name) ?: "")
        setAvatarUrl(
            typedArray.getString(R.styleable.UserProfileView_avatar_url)
                ?: "https://www.gravatar.com/avatar/${loginText}?s=$avatarSize&d=identicon&r=P"
        )
    }

    private fun findViews() {
        loginTextView = findViewById(R.id.login_text_view)
        companyTextView = findViewById(R.id.company_text_view)
        bioTextView = findViewById(R.id.bio_text_view)
        avatarImageView = findViewById(R.id.avatar_image_view)
        repoHolderView = findViewById(R.id.repo_holder)
    }
}