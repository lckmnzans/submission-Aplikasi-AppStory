package com.submission.appstory

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
            binding.tvAlert.text = "Anda sudah login"
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            binding.tvAlert.text = "Anda belum login"
        }

    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loginUser(email: String, password: String) {
        binding.loadLogin.visibility = View.VISIBLE
        val call = ApiConfig.getApiService().login(email, password)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.loadLogin.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    val token = response.body()?.loginResult?.token
                    binding.tvAlert.text = "Login sukses"
                    Log.d(TAG, "Token: $token")
                    saveSession(token)
                } else {
                    binding.tvAlert.text = response.body()?.message ?: "Login gagal"
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

    companion object {
        private const val TAG = "LoginActivity"
    }
}