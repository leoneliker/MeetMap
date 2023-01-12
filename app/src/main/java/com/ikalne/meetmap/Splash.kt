package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this@Splash, Initial::class.java)
            startActivity(intent)
        }, 3000)
    }
}