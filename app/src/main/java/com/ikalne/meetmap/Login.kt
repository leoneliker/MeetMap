package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
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
        login.setOnClickListener{
            login()
        }
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
                    showMapActivity()
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

    private fun showMapActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}