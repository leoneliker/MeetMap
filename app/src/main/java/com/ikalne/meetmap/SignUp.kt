package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val login = findViewById<Button>(R.id.btnSingUp)
        val cancel = findViewById<Button>(R.id.btncancel)

        login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        })
        cancel.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        })
    }
}