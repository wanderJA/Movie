package com.wander.baseframe.component

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.wander.baseframe.AndroidUtilities
import com.wander.baseframe.R
import com.wander.baseframe.context.AppContext
import com.wander.baseframe.utils.DebugLog
import com.wander.baseframe.utils.NetworkUtil
import kotlinx.android.synthetic.main.fragment_web.*
import org.json.JSONObject

open class CommonWebFragment : BaseLayerFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_web
    var title = "vipd"
    var mUrl = ""
        set(value) {
            field = createAppUrl(value)
        }

    private fun createAppUrl(value: String): String {
        val indexOf = value.indexOf("?")
        var url = value
        if (indexOf > 0) {
            url += "&isApp=1"
        } else {
            url += "?isApp=1"
        }
        return url
    }

    private var isErrorReceived: Boolean = false
    var fromViewpager = false

    companion object {
        const val WEB_URL = "WEB_URL"
        const val WEB_TITLE = "WEB_TITLE"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            mUrl = it.getString(WEB_URL, mUrl)
            title = it.getString(WEB_TITLE, "VIPD")
        }
        setCustomTitle(title)
        if (fromViewpager) {
            mTitleView?.setVisibleGone()
        }
        showTransparentLoading()
        initWeb()
    }

    @SuppressLint("AddJavascriptInterface")
    private fun initWeb() {
        //优化webview设置
        val webSettings = webView.settings
        webSettings.lightTouchEnabled = true
        webSettings.javaScriptEnabled = true
        webView.addJavascriptInterface(IqiyiJsBridge(), "IqiyiJsBridge")

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSettings.blockNetworkImage = true
        webSettings.textSize = WebSettings.TextSize.NORMAL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true

        webSettings.setAppCacheEnabled(true)
        val appCaceDir = mActivity?.getDir("cache", Context.MODE_PRIVATE)?.path
        webSettings.setAppCachePath(appCaceDir)

        webSettings.setAppCacheMaxSize((8 * 1024 * 1024).toLong())   //缓存最多可以有8M
        webSettings.allowFileAccess = true   // 可以读取文件缓存(manifest生效)
        val ua = webSettings.userAgentString

        //      原ua后拼接   ;iqiyi IqiyiApp/iqiyireader IqiyiReaderVersion/1.10.5.1
        val builder = StringBuilder()
        builder.append(if (TextUtils.isEmpty(ua)) "" else ua)
        webSettings.userAgentString = builder.toString()

        webSettings.useWideViewPort = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        if (!NetworkUtil.isNetworkAvailable(mActivity)) {
            showError()
        }


        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                view: WebView, errorCode: Int,
                description: String, failingUrl: String
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                isErrorReceived = true
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                DebugLog.d("shouldOverrideUrlLoading", request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }


            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                DebugLog.d(tagFragment, "onPageFinished")
                mUrl = url
                view.loadUrl("javascript:window.local_obj.getDesc(document.getElementsByName('description')[0].content);")
                view.loadUrl("javascript:window.local_obj.getImage(document.getElementsByTagName('img')[0].src);")

                if (isErrorReceived) {
                    showError()
                } else {
                    dismissTransparentLoading()
                    webView?.settings?.blockNetworkImage = false
                }

            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (mActivity == null || mActivity?.isDestroyed == true) {
                    return
                }
//                Logger.d(tag, "onProgressChanged$newProgress")
                if (!isErrorReceived && newProgress >= 95) {
                    dismissTransparentLoading()
                    webView?.settings?.blockNetworkImage = false
                }
                super.onProgressChanged(view, newProgress)
            }

            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                try {
                    val b2 = AlertDialog.Builder(mActivity)
                        .setTitle("Alert").setMessage(message)
                        .setPositiveButton("ok") { dialog, which -> result.confirm() }

                    b2.setCancelable(false)
                    b2.create()
                    b2.show()
                } catch (e: Exception) {
                }
                return true
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                setCustomTitle(title)
            }

        }

        loadWeb()

    }


    private val H5_CALLBACK_ARGS_TYPE = "type"
    private val JSBRIDGE_LOGIN = "JSBRIDGE_LOGIN"   //是否隐藏分享
    private val JSBRIDGE_HIDE_MENU = "JSBRIDGE_HIDE_MENU"   //隐藏所有右上角MENU
    private val JSBRIDGE_SHARE_DATA = "JSBRIDGE_SHARE_DATA" //设置分享信息

    inner class IqiyiJsBridge {
        @JavascriptInterface
        operator fun invoke(json: String) {
            try {
                val jsonObject = JSONObject(json)
                val type = jsonObject.getString(H5_CALLBACK_ARGS_TYPE)
                if (AppContext.isLog) {
                    DebugLog.d(tag, json)
                }
                val jsonRequest = jsonObject.getString("request")
                var request: JSONObject? = null
                if (!TextUtils.isEmpty(jsonRequest) && jsonRequest != "null") {
                    request = jsonObject.getJSONObject("request")
                }

                when (type) {
                    JSBRIDGE_LOGIN -> request?.let { handleLogin(it) }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        internal fun handleLogin(request: JSONObject) {
            try {
                AndroidUtilities.runOnUIThread {
                    val redirectUrlAfterLogin = request.optString("returnUrl")
                    val params = request.optString("params")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun showError() {
        showNetReload(object : ReloadListener {
            override fun onReload() {
                loadWeb()
            }
        })
    }


    fun loadWeb(url: String? = null) {
        url?.let { mUrl = url }
        if (mActivity == null) {
            return
        }
        isErrorReceived = false
//        if (!NetworkUtil.isNetworkAvailable(mActivity)) {
//            showError()
//            return
//        }
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = ""
            return
        }
        DebugLog.d(tagFragment, "url:$mUrl")
        webView.loadUrl(mUrl)
    }
}