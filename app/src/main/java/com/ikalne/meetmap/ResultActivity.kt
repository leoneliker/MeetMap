package com.ikalne.meetmap

import android.content.Intent
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
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnDeleteAccount = findViewById<Button>(R.id.btnDeleteAccount)
        val etemail = findViewById<TextView>(R.id.tvemail)
        val etpassword = findViewById<TextView>(R.id.tvpassword)
        btnDeleteAccount.setOnClickListener{
            prefs.wipe()
            initialActivity()
        }
        btnLogout.setOnClickListener{
            prefs.wipe()
            loginActivity()

        }
        val  userEmail= prefs.getEmail()
        etemail.text ="Bienvenido $userEmail"
        val  password= prefs.getPassword()
        etpassword.text ="Tu password es $password"
    }

    fun loginActivity()
    {
        startActivity(Intent(this,Login::class.java ))
    }
    fun initialActivity()
    {
        startActivity(Intent(this,Initial::class.java ))
    }
}