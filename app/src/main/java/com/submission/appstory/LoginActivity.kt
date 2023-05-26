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
import androidx.lifecycle.ViewModelProvider
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityLoginBinding
import com.submission.appstory.response.LoginResponse
import com.submission.appstory.viewModel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (viewModel.isEmailValid(email)) {
                    binding.tvEmailAlert.text = null
                } else {
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

            viewModel.loginUser(email, password)
            viewModel.isLoading.observe(this) {isLoading -> showLoading(isLoading)}
            viewModel.isSuccess.observe(this) {isSuccess -> showLoginResponse(isSuccess)}
            viewModel.token.observe(this) {token -> saveSession(token)}
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadLogin.visibility = View.VISIBLE
        } else {
            binding.loadLogin.visibility = View.INVISIBLE
        }
    }

    private fun showLoginResponse(isSuccess: Boolean) {
        if (isSuccess) {
            Toast.makeText(this@LoginActivity, "Login sukses", Toast.LENGTH_SHORT).show()
            toMainActivity()
        } else {
            binding.tvPasswordAlert.visibility = View.VISIBLE
            binding.tvPasswordAlert.text = "Login gagal. Silahkan coba lagi atau sesuaikan email atau password anda."
            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }
}