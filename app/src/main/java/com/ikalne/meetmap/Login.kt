package com.ikalne.meetmap

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        val login = findViewById<Button>(R.id.btnlogin)
        val cancel = findViewById<Button>(R.id.btncancel)
        login.setOnClickListener { login() }
        cancel.setOnClickListener{
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        }
    }

    fun login(){
        val emailTIL = findViewById<TextInputLayout>(R.id.etemail)
        val passwordTIL = findViewById<TextInputLayout>(R.id.etpassword)
        if (email.text.isEmpty() && password.text.isEmpty()){
            showError(emailTIL, "This field can´t be empty")
            showError(passwordTIL, "This field can´t be empty")
        }else if(!email.text.contains("@")){
            showError(emailTIL, "Email is not valid")
        }else{
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    PreferencesManager.getDefaultSharedPreferences(this).saveEmail(email.text.toString())
                    PreferencesManager.getDefaultSharedPreferences(this).savePass(password.text.toString())
                    val intent = Intent(this@Login, MainAppActivity::class.java)
                    startActivity(intent)
                }else{
                    //showAlert()
                    showError(emailTIL, "Incorrect email or password")
                }
            }
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticación al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showError(textInputLayout: TextInputLayout, error: String){
        textInputLayout.error = error
    }

}