package com.wander.movie.ui.activity

import android.os.Bundle
import com.wander.baseframe.component.BaseActivity
import com.wander.baseframe.utils.SystemBarUtil
import com.wander.movie.App
import com.wander.movie.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import kotlin.math.min

class FlashActivity : BaseActivity() {
    private val showTime = 200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)
        SystemBarUtil.hideSystemBar(mActivity)
        GlobalScope.launch {
            delay(showTime - min(showTime, System.currentTimeMillis() - App.startTime))
            startActivity<MainActivity>()
            finish()
        }

    }
}
