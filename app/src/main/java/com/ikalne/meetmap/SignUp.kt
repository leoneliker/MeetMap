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
        val cancel = findViewById<Button>(R.id.btncancel)
        signup.setOnClickListener{accessToDetail()}
        cancel.setOnClickListener{initialActivity()}
    }

    fun accessToDetail()
    {
        val email =findViewById<EditText>(R.id.email)
        val password =findViewById<EditText>(R.id.password)
        val repassword =findViewById<EditText>(R.id.repassword)
        if(email.text.toString().isNotEmpty()&&password.text.toString().isNotEmpty()&&repassword.text.toString().isNotEmpty()) //falta añadir que nada este vacio
        { /*codigo nerea rama database dev*/
            prefs.saveEmail(email.text.toString())
            prefs.savePass(password.text.toString())
            prefs.saveRePass(repassword.text.toString())
            goToDetail()
        }
        else
        {
            //hacer otra cosa
            //mensaje de que esta vacio (toast, error...)
        }
    }

    fun goToDetail()
    {
        startActivity(Intent(this,MainAppActivity::class.java ))
    }
    fun initialActivity()
    {
        startActivity(Intent(this,Initial::class.java ))
    }
}