package com.example.khajakhoj.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.khajakhoj.R

object MapUtils {
    fun showMapsWebViewDialog(context: Context,mapUrl:String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.maps_web_view)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)

        val webView = dialog.findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl(mapUrl)

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE



        dialog.show()
    }
}