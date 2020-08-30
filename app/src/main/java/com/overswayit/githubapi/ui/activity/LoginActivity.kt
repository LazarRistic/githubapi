package com.overswayit.githubapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.overswayit.githubapi.GitHubAPIApp
import com.overswayit.githubapi.R
import com.overswayit.githubapi.sharedprefs.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.username_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val pass = passwordEditText.text.toString()

            if (TextUtils.isEmpty(username) && TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Username and password must be entered", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val credentials = Credentials.basic(username, pass)

                lifecycleScope.launch(Dispatchers.IO) {
                    val result =
                        (application as GitHubAPIApp).gitHubAPIService.testCredentials(credentials)

                    result.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful && response.code() == 200) {
                                SharedPreference.save("CREDENTIALS", credentials)

                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    response.errorBody()?.string(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
                }
            }
        }
    }
}