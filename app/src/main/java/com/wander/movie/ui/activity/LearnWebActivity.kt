package com.wander.movie.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wander.baseframe.component.BaseActivity
import com.wander.movie.R
import kotlinx.android.synthetic.main.activity_learn_web.*

open class LearnWebActivity : BaseActivity() {

    val URL = "mUrl"
    val TITLE = "title"

    private var mUrl: String? = null

    fun runActivity(context: Context, title: String, url: String) {
        val intent = Intent(context, LearnWebActivity::class.java)
        intent.putExtra(URL, url)
        intent.putExtra(TITLE, title)
        context.startActivity(intent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_web)
        mUrl = intent.getStringExtra(URL)
        val title = intent.getStringExtra(TITLE)
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webProgress.max = 100
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                webProgress!!.progress = newProgress
                if (newProgress >= 100) {
                    webProgress!!.visibility = View.GONE
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                mUrl = url
                return true
            }
        }
        webView!!.loadUrl(mUrl)

        webShareFloat.setOnClickListener { share() }
    }

    private fun share() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, mUrl)
        intent.type = "text/plain"
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(intent, "分享到"))
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
            return
        }
        super.onBackPressed()
    }

}
