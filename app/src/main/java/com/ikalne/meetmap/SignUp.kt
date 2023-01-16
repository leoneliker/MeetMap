package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
        if(prefs.getName().isNotEmpty())
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
        val name =findViewById<EditText>(R.id.etusername)
        val password =findViewById<EditText>(R.id.etpassword)
        val repassword =findViewById<EditText>(R.id.etrepassword)
        if(name.text.toString().isNotEmpty())
        {
            prefs.saveName(name.text.toString())
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
        startActivity(Intent(this,MapsActivity::class.java ))
    }
}