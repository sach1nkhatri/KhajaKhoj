package com.example.khajakhoj.utils

import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import com.example.khajakhoj.R

object PasswordVisibilityToggler {
    fun togglePasswordVisibility(editText: EditText, toggleButton: ImageView) {
        val currentInputType = editText.inputType
        editText.inputType =
            if (currentInputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

        toggleButton.setImageResource(
            if (currentInputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                R.drawable.ic_eye_off
            } else {
                R.drawable.ic_eye
            }
        )

        editText.setSelection(editText.text.length)
    }
}