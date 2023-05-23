package com.submission.appstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.submission.appstory.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    binding.textViewError.text = "Password must be at least 8 characters"
                    binding.textViewError.visibility = TextView.VISIBLE
                } else {
                    binding.textViewError.visibility = TextView.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editTextConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.editTextPassword.text.toString()
                val confirmPassword = s.toString()

                if (password != confirmPassword) {
                    binding.textView2Error.text = "Passwords do not match"
                    binding.textView2Error.visibility = TextView.VISIBLE
                } else {
                    binding.textView2Error.visibility = TextView.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val passwordConfirmation = binding.editTextConfirmPassword.text.toString()

            if (password == passwordConfirmation) {
                binding.textViewError.visibility = TextView.GONE
                binding.textView2Error.visibility = TextView.GONE
                registerUser(name, email, password)
            } else {
                binding.textView2Error.text = "Password does not match"
            }
            // Lakukan validasi lainnya dan lakukan registrasi
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        // Implementasi logika untuk registrasi pengguna di sini
    }
}