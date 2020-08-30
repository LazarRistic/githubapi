package com.overswayit.githubapi.util

import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.text.toSpannable
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.entity.Commit


object StringUtils {

    fun commitAuthorsName(commit: Commit): Spannable {
        val context = GitHubAPIApp.applicationContext()
        val blackColor = context.getColor(R.color.black)
        val grayColor = context.getColor(R.color.sub_text)

        val authored = context.getString(R.string.authored)
        val and = context.getString(R.string.and)
        val committed = context.getString(R.string.committed)

        val authLoginSpan = SpannableStringBuilder(commit.authorLogin)
        authLoginSpan.setSpan(ForegroundColorSpan(blackColor), 0, authLoginSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        authLoginSpan.setSpan(StyleSpan(BOLD), 0, authLoginSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val authEmailSpan = SpannableStringBuilder("<${commit.authorEmail}>")
        authEmailSpan.setSpan(ForegroundColorSpan(blackColor), 0, authEmailSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val authoredSpan = SpannableStringBuilder(authored)
        authoredSpan.setSpan(ForegroundColorSpan(grayColor), 0, authoredSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val resultSpan = SpannableStringBuilder()
        resultSpan.append(authLoginSpan)
        resultSpan.append(" ").append(authEmailSpan)
        resultSpan.append(" ").append(authoredSpan)

        if (!TextUtils.isEmpty(commit.committerLogin)) {
            val commiterSpan = SpannableStringBuilder()

            val andSpan = SpannableStringBuilder(and)
            andSpan.setSpan(ForegroundColorSpan(grayColor), 0, andSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val comLoginSpan = SpannableStringBuilder(commit.committerLogin)
            comLoginSpan.setSpan(ForegroundColorSpan(blackColor), 0, comLoginSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            comLoginSpan.setSpan(StyleSpan(BOLD), 0, comLoginSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val comEmailSpan = SpannableStringBuilder("<${commit.committerEmail}>")
            comEmailSpan.setSpan(ForegroundColorSpan(blackColor), 0, comEmailSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val committedSpan = SpannableStringBuilder(committed)
            committedSpan.setSpan(ForegroundColorSpan(grayColor), 0, committedSpan.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            commiterSpan.append(andSpan)
            commiterSpan.append(" ").append(comLoginSpan)
            commiterSpan.append(" ").append(comEmailSpan)
            commiterSpan.append(" ").append(committedSpan)
            resultSpan.append(" ").append(commiterSpan)
        }

        return resultSpan.toSpannable()
    }

    fun stringifyParentsList(parents: List<Pair<String, String>>): String {
        val parentsSpan = SpannableStringBuilder()

        parents.forEach {
            parentsSpan.append("[").append(it.second).append("]").append(" - ")
        }

        if (parentsSpan.length >= 3) {
            parentsSpan.delete(parentsSpan.length - 3, parentsSpan.length)
        }

        return parentsSpan.toString()
    }


}