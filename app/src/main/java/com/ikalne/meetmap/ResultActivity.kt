package com.ikalne.meetmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ikalne.meetmap.MeetMapApplication.Companion.prefs

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initUI()
    }

    fun initUI()
    {
        val btnBack = findViewById<Button>(R.id.btnBack)
        val etemail = findViewById<TextView>(R.id.tvemail)
        val etpassword = findViewById<TextView>(R.id.tvpassword)
        btnBack.setOnClickListener{
            prefs.wipe()
            onBackPressed()
        }
        val  userEmail= prefs.getEmail()
        etemail.text ="Bienvenido $userEmail"
        val  password= prefs.getPassword()
        etpassword.text ="Tu password es $password"
    }
}