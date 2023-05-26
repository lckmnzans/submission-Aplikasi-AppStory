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
import androidx.lifecycle.ViewModelProvider
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityRegisterBinding
import com.submission.appstory.response.RegisterRequest
import com.submission.appstory.response.RegisterResponse
import com.submission.appstory.viewModel.LoginViewModel
import com.submission.appstory.viewModel.RegisterViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (viewModel.isEmailValid(email)) {
                    binding.tvEmailAlert.text = ""
                } else {
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
                viewModel.registerUser(name, email, password)
                viewModel.isSuccess.observe(this) {isSuccess -> showRegisterResponse(isSuccess)}
            }
        }
    }

    private fun showRegisterResponse(isSuccess: Boolean) {
        if (isSuccess) {
            Toast.makeText(this@RegisterActivity, "Register sukses", Toast.LENGTH_SHORT).show()
        } else {
            binding.tvPasswordAlert.visibility = View.VISIBLE
            binding.tvPasswordAlert.text = "Register gagal. Silahkan coba lagi atau sesuaikan email anda."
            Toast.makeText(this@RegisterActivity, "Register gagal", Toast.LENGTH_SHORT).show()
        }
    }
}