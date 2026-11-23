package com.example.helloworld

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val greeting = TextView(this).apply {
            gravity = Gravity.CENTER
            textSize = 24f
            setText(R.string.hello_world)
        }
        setContentView(greeting)
    }
}
