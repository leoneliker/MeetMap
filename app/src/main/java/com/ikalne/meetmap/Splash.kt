package com.ikalne.meetmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openApp();
    }
    private fun openApp() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this@Splash, Initial::class.java)
            startActivity(intent)
        }, 2000)
    }
}