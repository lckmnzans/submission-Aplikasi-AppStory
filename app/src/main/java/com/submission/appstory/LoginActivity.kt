package com.submission.appstory

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityLoginBinding
import com.submission.appstory.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (isEmailValid(email)) {
                    // Email valid, lakukan tindakan yang sesuai
                    binding.tvEmailAlert.text = null
                } else {
                    // Email tidak valid, tampilkan pesan kesalahan
                    binding.tvEmailAlert.text = "Email belum valid"
                }
            }

        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    binding.tvPasswordAlert.text = "Password minimal 8 karakter"
                    binding.tvPasswordAlert.visibility = TextView.VISIBLE
                } else {
                    binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            loginUser(email, password)
        }

        binding.btnCreateAccount.setOnClickListener {
            navigateToRegister()
        }

        val isLoggedIn = getSharedPreferences("LoginSession", Context.MODE_PRIVATE).getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            toMainActivity()
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loginUser(email: String, password: String) {
        binding.loadLogin.visibility = View.VISIBLE
        val call = ApiConfig.getApiService("").login(email, password)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.loadLogin.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val token = responseBody.loginResult?.token
                        Log.d(TAG, "Token: $token")
                        Toast.makeText(this@LoginActivity, "Login sukses", Toast.LENGTH_SHORT).show()
                        saveSession(token)
                        toMainActivity()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, response.body()?.message ?: "Login gagal", Toast.LENGTH_SHORT).show()
                    binding.tvPasswordAlert.visibility = View.VISIBLE
                    binding.tvPasswordAlert.text = "Login gagal. Silahkan coba lagi atau sesuaikan email atau password anda."
                    Log.e(TAG, "onResponse: ${response.body()?.message ?: "Login gagal"}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.loadLogin.visibility = View.INVISIBLE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun saveSession(token: String?) {
        val sharedPreferences = getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("token", token)
        editor.apply()
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern1 = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val emailPattern2 = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}+\\.[a-zA-Z]{2,}+\\.[a-zA-Z]{2,}")
        return email.matches(emailPattern1) || email.matches(emailPattern2)
    }
    companion object {
        private const val TAG = "LoginActivity"
    }
}