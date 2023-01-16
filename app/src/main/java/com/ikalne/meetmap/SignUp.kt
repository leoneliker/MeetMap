package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.ikalne.meetmap.MeetMapApplication.Companion.prefs

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initUI()
        checkUserValues()

    }

    fun checkUserValues()
    {
        if(prefs.getEmail().isNotEmpty())
        {
            goToDetail()
        }
    }
    fun initUI()
    {
       val signup = findViewById<Button>(R.id.btnSignUp)
       signup.setOnClickListener{accessToDetail()}
    }

    fun accessToDetail()
    {
        val email =findViewById<EditText>(R.id.email)
        val password =findViewById<EditText>(R.id.password)
        val repassword =findViewById<EditText>(R.id.repassword)
        if(email.text.toString().isNotEmpty())
        {
            prefs.saveEmail(email.text.toString())
            prefs.savePass(password.text.toString())
            prefs.saveRePass(repassword.text.toString())
            goToDetail()
        }
        else
        {
            //hacer otra cosa
        }
    }

    fun goToDetail()
    {
        startActivity(Intent(this,ResultActivity::class.java ))
    }
}