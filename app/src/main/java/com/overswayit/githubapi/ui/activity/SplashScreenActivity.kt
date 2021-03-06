package com.overswayit.githubapi.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.viewmodel.SplashScreenViewModel
import com.overswayit.githubapi.ui.viewmodel.SplashScreenViewModelFactory

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var progressBar: ContentLoadingProgressBar

    private val viewModel by viewModels<SplashScreenViewModel> {
        SplashScreenViewModelFactory(
            (applicationContext as GitHubAPIApp).userRepository,
            (applicationContext as GitHubAPIApp).repoRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        progressBar = findViewById(R.id.progress_bar)

        viewModel.fetchInformation()

        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)

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