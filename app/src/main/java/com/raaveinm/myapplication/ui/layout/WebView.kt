package com.raaveinm.myapplication.ui.layout

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreenMain(modifier: Modifier = Modifier){ Box (modifier){ WebViewScreen() } }
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen (){
    AndroidView(
        factory = {
            context -> WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://spotify.com")
            }
        }
    )
}
