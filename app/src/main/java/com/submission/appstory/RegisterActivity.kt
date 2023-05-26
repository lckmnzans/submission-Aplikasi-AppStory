package com.submission.appstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityRegisterBinding
import com.submission.appstory.response.AddStoryResponse
import com.submission.appstory.response.RegisterRequest
import com.submission.appstory.response.RegisterResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    binding.tvError.text = "Password must be at least 8 characters"
                    binding.tvError.visibility = TextView.VISIBLE
                } else {
                    binding.tvError.visibility = TextView.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterPasswordConfirmation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.edRegisterPassword.toString()
                val confirmPassword = s.toString()

                if (password != confirmPassword) {
                    binding.tvError2.text = "Passwords do not match"
                    binding.tvError2.visibility = TextView.VISIBLE
                } else {
                    binding.tvError2.visibility = TextView.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val passwordConfirmation = binding.edRegisterPasswordConfirmation.text.toString()

            if (password == passwordConfirmation) {
                binding.tvError.visibility = TextView.GONE
                binding.tvError2.visibility = TextView.GONE
                registerUser(name, email, password)
            } else {
                binding.tvError2.text = "Password do not match"
            }
            // Lakukan validasi lainnya dan lakukan registrasi
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        //
    }
}