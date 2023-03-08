package com.tsu.wordsfactory

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlin.Boolean

private val URL = "https://learnenglish.britishcouncil.org/general-english/video-zone"
class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
       /* return super.shouldOverrideUrlLoading(view, request)*/

        println(request?.url.toString())
        if (URL in request?.url.toString()) {
            return false
        }
        return true
    }
}