package com.overswayit.githubapi.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.overswayit.githubapi.GitHubAPIApp

class SharedPreference {

    companion object {
        private const val PREFS_NAME = "githubapi_shared_prefs"
        private val sharedPref: SharedPreferences = GitHubAPIApp.applicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        fun save(KEY_NAME: String, value: String) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString(KEY_NAME, value)
            editor.apply()
        }

        fun getString(KEY_NAME: String): String? {
            return sharedPref.getString(KEY_NAME, null)
        }

        @Suppress("unused")
        fun remove(KEY_NAME: String) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.remove(KEY_NAME)
            editor.apply()
        }
    }


}