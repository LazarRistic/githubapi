package com.overswayit.githubapi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel: ViewModel() {

    @Suppress("PrivatePropertyName")
    private val LOG_VIEW_MODEL = false

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "UserDetailsViewModel"

    val userLogin = "octocat"

    val ioScope = CoroutineScope(Dispatchers.IO)

    fun logDebug(error: String) {
        if (LOG_VIEW_MODEL) {
            Log.d(LOG_TAG, error)
        }
    }
}