package com.wander.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.shadow.ShadowDrawableWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        content.background = ShadowDrawableWrapper(this,getDrawable(R.drawable.transprant_drawable),20f,40f,80f)
    }
}
