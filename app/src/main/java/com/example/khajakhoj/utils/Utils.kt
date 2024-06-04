package com.example.khajakhoj.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.khajakhoj.Dashboard
import com.example.khajakhoj.LoginPage
import com.example.khajakhoj.R

object Utils {
    fun logOut(context: Context){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Logout")
        alertDialogBuilder.setMessage("Do you want to log out?")
        alertDialogBuilder.setPositiveButton("Yes"){_,_->
            context.startActivity(Intent(context, LoginPage::class.java))
            if (context is AppCompatActivity) {
                context.finish()
            }

        }
        alertDialogBuilder.setNegativeButton("No",null)
        val alertDialog = alertDialogBuilder.create()
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        alertDialog.window?.setBackgroundDrawable(backgroundDrawable)
        alertDialog.show()
    }
}