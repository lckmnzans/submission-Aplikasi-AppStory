package com.submission.appstory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityRegisterBinding
import com.submission.appstory.response.RegisterRequest
import com.submission.appstory.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (isEmailValid(email)) {
                    // Email valid, lakukan tindakan yang sesuai
                    binding.tvEmailAlert.text = ""
                } else {
                    // Email tidak valid, tampilkan pesan kesalahan
                    binding.tvEmailAlert.text = "Email belum valid"
                }
            }

        })
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    binding.tvPasswordAlert.text = "Password harus minimal 8 karakter"
                    binding.tvPasswordAlert.visibility = View.VISIBLE
                } else {
                    binding.tvPasswordAlert.text = "Password sudah sesuai"
                    binding.tvPasswordAlert.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterPasswordConfirmation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    binding.tvPassword2Alert.text = "Password harus minimal 8 karakter"
                    binding.tvPassword2Alert.visibility = View.VISIBLE
                } else {
                    if (binding.edRegisterPassword.text.toString() != password) {
                        binding.tvPassword2Alert.text = "Password belum sesuai"
                        binding.tvPassword2Alert.visibility = View.VISIBLE
                    } else {
                        binding.tvPassword2Alert.text = "Password sudah sesuai"
                        binding.tvPassword2Alert.visibility = View.INVISIBLE
                    }
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
                binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                binding.tvPassword2Alert.visibility = TextView.INVISIBLE
                registerUser(name, email, password)
            }
            // Lakukan validasi lainnya dan lakukan registrasi
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val regRequest = RegisterRequest(name, email, password)
        val call = ApiConfig.getApiService("").register(regRequest)
        call.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(this@RegisterActivity, "Register sukses", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onResponse: ${responseBody.message}")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        } )
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern1 = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val emailPattern2 = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}+\\.[a-zA-Z]{2,}+\\.[a-zA-Z]{2,}")
        return email.matches(emailPattern1) || email.matches(emailPattern2)
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}