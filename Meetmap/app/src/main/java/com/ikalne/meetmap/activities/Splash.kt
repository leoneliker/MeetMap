package com.ikalne.meetmap.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.ikalne.meetmap.*
import com.ikalne.meetmap.models.PreferencesManager
import java.util.*

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

        val animLeftHand = AnimationUtils.loadAnimation(this, R.anim.move_left_to_right)
        val animRightHand = AnimationUtils.loadAnimation(this, R.anim.move_right_to_left)
        val animtext = AnimationUtils.loadAnimation(this, R.anim.fadein)

        title.visibility =  View.INVISIBLE
        slogan.visibility = View.INVISIBLE
        val currentLanguage: String = Locale.getDefault().language

        if (currentLanguage == "es") {
            slogan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f) // Cambia el tamaño a tu preferencia
        }

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
        }, 4000)
    }

    override fun onPause() {
        super.onPause()
    }
}