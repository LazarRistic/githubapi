package com.overswayit.githubapi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

abstract class BasicViewModel: ViewModel() {

    @Suppress("PrivatePropertyName")
    private val LOG_VIEW_MODEL = false

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "UserDetailsViewModel"

    val userLogin = "octocat"

    fun logDebug(error: String) {
        if (LOG_VIEW_MODEL) {
            Log.d(LOG_TAG, error)
        }
    }
}