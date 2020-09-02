package com.overswayit.githubapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.ui.viewmodel.SplashScreenViewModel
import com.overswayit.githubapi.ui.viewmodel.SplashScreenViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.InetAddress

class SplashScreenActivity : BaseActivity() {

    private lateinit var progressBar: ContentLoadingProgressBar

    private val disposable = CompositeDisposable()

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

    override fun onStart() {
        super.onStart()

        disposable.add(viewModel.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            })
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
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