package com.ikalne.meetmap

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        forceLightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        openApp()

        val leftHand = findViewById<ImageView>(R.id.leftHand)
        val rightHand = findViewById<ImageView>(R.id.rightHand)
        val title = findViewById<ImageView>(R.id.meetmap)
        val slogan = findViewById<TextView>(R.id.slogan)
        val text = slogan.text.toString()

        val spannableString = SpannableString(text)
        val words = text.split(" ")

        var colorIndex = 0
        var startIndex = 0

        for (word in words) {
            val endIndex = startIndex + word.length

            val color = if (colorIndex % 2 == 0) {
                R.color.primary_dark
            } else {
               R.color.secondary
            }
            val colors = ContextCompat.getColor(this, color)

            // Cambia el color de cada palabra
            spannableString.setSpan(
                ForegroundColorSpan(colors),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            startIndex = endIndex + 1 // Añade 1 para ignorar el espacio
            colorIndex++
        }
        slogan.text = spannableString
        val animLeftHand = AnimationUtils.loadAnimation(this, R.anim.move_left_to_right)
        val animRightHand = AnimationUtils.loadAnimation(this, R.anim.move_right_to_left)
        val animtext = AnimationUtils.loadAnimation(this, R.anim.fadein)

        title.visibility =  View.INVISIBLE
        slogan.visibility = View.INVISIBLE

        val handsAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Se ejecuta cuando la animación de las manos termina
                title.visibility = View.VISIBLE
                slogan.visibility = View.VISIBLE
                title.startAnimation(animtext)
                slogan.startAnimation(animtext)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        }

// Establece el AnimationListener en ambas animaciones de las manos
        animLeftHand.setAnimationListener(handsAnimationListener)
        animRightHand.setAnimationListener(handsAnimationListener)

// Inicia las animaciones de las manos
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
        }, 4500)
    }

    override fun onPause() {
        super.onPause()
    }
}