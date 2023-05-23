package com.submission.appstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var textViewError: TextView
    private lateinit var buttonRegister: Button
    private lateinit var textView2Error: TextView
    private lateinit var editTextConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        textViewError = findViewById(R.id.textViewError)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        textView2Error = findViewById(R.id.textView2Error)
        buttonRegister = findViewById(R.id.buttonRegister)

        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    textViewError.text = "Password must be at least 8 characters"
                    textViewError.visibility = TextView.VISIBLE
                } else {
                    textViewError.visibility = TextView.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editTextConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = editTextPassword.text.toString()
                val confirmPassword = s.toString()

                if (password != confirmPassword) {
                    textView2Error.text = "Passwords do not match"
                    textView2Error.visibility = TextView.VISIBLE
                } else {
                    textView2Error.visibility = TextView.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val passwordConfirmation = editTextConfirmPassword.text.toString()

            if (password == passwordConfirmation) {
                textViewError.visibility = TextView.GONE
                textView2Error.visibility = TextView.GONE
                registerUser(name, email, password)
            } else {
                textView2Error.text = "Password does not match"
            }
            // Lakukan validasi lainnya dan lakukan registrasi
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        // Implementasi logika untuk registrasi pengguna di sini
    }
}