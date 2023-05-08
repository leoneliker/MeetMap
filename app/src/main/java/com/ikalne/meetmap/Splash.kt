package com.ikalne.meetmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        forceLightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        openApp()

        val leftHand = findViewById<ImageView>(R.id.leftHand)
        val rightHand = findViewById<ImageView>(R.id.rightHand)
        val mUniverse = findViewById<ImageView>(R.id.backView)

        val animLeftHand = AnimationUtils.loadAnimation(this, R.anim.move_left_to_right)
        val animRightHand = AnimationUtils.loadAnimation(this, R.anim.move_right_to_left)
        val animBack = AnimationUtils.loadAnimation(this, R.anim.fadeinout)
        leftHand.visibility = View.INVISIBLE
        rightHand.visibility = View.INVISIBLE
        animBack.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                leftHand.visibility = View.VISIBLE
                rightHand.visibility = View.VISIBLE
                leftHand.startAnimation(animLeftHand)
                rightHand.startAnimation(animRightHand)
            }
        })

        mUniverse.startAnimation(animBack)



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
        }, 10000)
    }

    override fun onPause() {
        super.onPause()
    }
}