package com.overswayit.githubapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.overswayit.githubapi.R
import com.overswayit.githubapi.sharedprefs.SharedPreference

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var progressBar: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        progressBar = findViewById(R.id.progress_bar)

        val credentials = SharedPreference.getString("CREDENTIALS")
        val intent: Intent

        intent = if (TextUtils.isEmpty(credentials)) {
            Intent(this@SplashScreenActivity, LoginActivity::class.java)
        } else {
            Intent(this@SplashScreenActivity, SearchUsersActivity::class.java)
        }

        Thread {
            doLoadAnimation()
            startActivity(intent)
            finish()
        }.start()
    }

    private fun doLoadAnimation() {
        var progress = 2
        while (progress < 100) {
            try {
                Thread.sleep(50)
                progressBar.progress = progress
            } catch (e: Exception) {
                e.printStackTrace()
            }
            progress += 2
        }
    }
}