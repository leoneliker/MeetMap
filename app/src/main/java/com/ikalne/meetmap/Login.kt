package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class Login : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()
        checkUserValues()
    }

    fun checkUserValues()
    {
        if (MeetMapApplication.prefs.getEmail().isNotEmpty())
        {
            goToDetail()
        }
    }

    fun initUI()
    {
        val login = findViewById<Button>(R.id.btnlogin)
        val cancel = findViewById<Button>(R.id.btncancel)
        login.setOnClickListener { accessToDetail() }
        cancel.setOnClickListener{initialActivity()}
    }

    fun accessToDetail()
    {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty())
        {
            MeetMapApplication.prefs.saveEmail(email.text.toString())
            MeetMapApplication.prefs.savePass(password.text.toString())
            goToDetail()
        }
        else
        {
            //hacer otra cosa
            //mensaje de que falta algo por rellenar
        }
    }

    fun goToDetail()
    {
        startActivity(Intent(this, MainAppActivity::class.java))
    }
    fun initialActivity()
    {
        startActivity(Intent(this,Initial::class.java ))
    }
}