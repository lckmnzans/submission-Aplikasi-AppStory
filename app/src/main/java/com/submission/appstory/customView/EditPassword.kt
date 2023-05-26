package com.submission.appstory.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.submission.appstory.R

class EditPassword: AppCompatEditText {
    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private var isPasswordVisible: Boolean = false
    private val showPasswordDrawable: Drawable? = ContextCompat.getDrawable(context,
        R.drawable.ic_visibility_show
    )
    private val hidePasswordDrawable: Drawable? = ContextCompat.getDrawable(context,
        R.drawable.ic_visibility_hide
    )

    private fun init() {
        // Set default input type to textPassword
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Add a TextWatcher to monitor text changes
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Show or hide the password visibility toggle icon based on the input length
                val showIcon = s?.isNotEmpty() ?: false
                val endDrawable = if (showIcon) showPasswordDrawable else null
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, endDrawable, null)
            }
        })

        // Set a click listener to toggle password visibility when the end drawable is clicked
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= right - compoundPaddingEnd) {
                togglePasswordVisibility()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    private fun togglePasswordVisibility() {
        // Toggle password visibility and update the input type accordingly
        isPasswordVisible = !isPasswordVisible
        val inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setInputType(inputType)

        // Update the end drawable based on the password visibility
        val endDrawable = if (isPasswordVisible) hidePasswordDrawable else showPasswordDrawable
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, endDrawable, null)
    }
}