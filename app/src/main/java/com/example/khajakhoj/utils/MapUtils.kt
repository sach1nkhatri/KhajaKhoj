package com.example.khajakhoj.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.khajakhoj.R

object MapUtils {
    fun showMapsWebViewDialog(context: Context, mapUrl: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.maps_web_view)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)

        val webView = dialog.findViewById<WebView>(R.id.webView)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (it.startsWith("http://") || it.startsWith("https://")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(intent)
                        return true
                    }
                }
                return false
            }
        }
        webView.loadUrl(mapUrl)
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

        dialog.show()
    }
}
