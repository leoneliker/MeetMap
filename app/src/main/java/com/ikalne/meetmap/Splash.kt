package com.ikalne.meetmap

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        forceLightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        openApp()

        val leftHand = findViewById<ImageView>(R.id.leftHand)
        val rightHand = findViewById<ImageView>(R.id.rightHand)

        val animLeftHand = AnimationUtils.loadAnimation(this, R.anim.move_left_to_right)
        val animRightHand = AnimationUtils.loadAnimation(this, R.anim.move_right_to_left)

        leftHand.startAnimation(animLeftHand)
        rightHand.startAnimation(animRightHand)

    }

    private fun openApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            PreferencesManager.getDefaultSharedPreferences(this).apply {
                intent = if (getEmail().isNotEmpty()) {
                    Intent(this@Splash, MainAppActivity::class.java)
                } else {
                    Intent(this@Splash, Initial::class.java)

                }
                startActivity(intent)
            }
        }, 3000)
    }

    override fun onPause() {
        super.onPause()
        Log.i("Pito","Pito")
    }
}