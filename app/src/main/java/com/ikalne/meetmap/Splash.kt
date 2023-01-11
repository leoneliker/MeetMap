package com.ikalne.meetmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var leftHand = findViewById<ImageView>(R.id.leftHand)
        var rightHand = findViewById<ImageView>(R.id.rightHand)

        var animLeftHand = AnimationUtils.loadAnimation(this, R.anim.move_left_to_right)
        var animRightHand = AnimationUtils.loadAnimation(this, R.anim.move_right_to_left)

        leftHand.startAnimation(animLeftHand)
        rightHand.startAnimation(animRightHand)

    }
}