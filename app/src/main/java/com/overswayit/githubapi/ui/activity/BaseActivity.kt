package com.overswayit.githubapi.ui.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @Suppress("PrivatePropertyName")
    private val LOG_VIEW_MODEL = true

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "LAZA BaseViewModel"

    fun logDebug(error: String) {
        if (LOG_VIEW_MODEL) {
            Log.d(LOG_TAG, error)
        }
    }

}