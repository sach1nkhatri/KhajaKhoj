package com.example.khajakhoj.utils

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

object VulgarityUtils {
    private lateinit var requestQueue: RequestQueue

    fun initialize(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
    }

    fun checkVulgarity(reviewText: String, callback: (Boolean) -> Unit) {
        val url = "https://www.purgomalum.com/service/containsprofanity?text=$reviewText"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val isProfane = response.trim().toBoolean()
                callback(!isProfane)
            },
            { error ->
                Log.e("VulgarityUtils", "Error checking vulgarity: ${error.message}")
                callback(true)
            })

        requestQueue.add(stringRequest)
    }
}
