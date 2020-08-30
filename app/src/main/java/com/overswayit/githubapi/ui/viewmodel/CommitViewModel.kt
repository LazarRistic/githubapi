package com.overswayit.githubapi.ui.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.overswayit.githubapi.entity.Commit
import com.overswayit.githubapi.util.DateUtils
import com.overswayit.githubapi.util.StringUtils

class CommitViewModel(private val commit: Commit) {

    fun message(): String {
       if (TextUtils.isEmpty(commit.message)) {
           return ""
       }

        return commit.message
    }

    fun date(context: Context): String {
        if (commit.authorDate == null) {
            return "0s"
        }

        return DateUtils.toStatusTimeString(context, commit.authorDate).toString()
    }

    fun authorImage(context: Context, imageView: ImageView) {
        Glide.with(context).load(commit.authorAvatarUrl).circleCrop().into(object: CustomTarget<Drawable>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                imageView.setImageDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                imageView.setImageDrawable(placeholder)
            }

        })
    }

    fun author(): Spannable {
        return StringUtils.commitAuthorsName(commit)
    }

    fun sha(): String {
        return "[${commit.shaShort}]"
    }

    fun parentsSha(): String {
        return StringUtils.stringifyParentsList(commit.parents)
    }
}
