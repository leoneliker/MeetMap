package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        val login = findViewById<Button>(R.id.btnlogin)
        val cancel = findViewById<Button>(R.id.btncancel)


        login.setOnClickListener{
            login()
        }
        cancel.setOnClickListener{
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        }
    }
    fun login(){
        if (email.text.isEmpty() && !email.text.contains("@")){
            showError(email, "Email is not valid")
        }else if(password.text.isEmpty()){
            showError(password, "Password can't be empty")
        }else{ //if (email.text.isNotEmpty() && password.text.isNotEmpty())
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    showMapActivity()
                }else{
                    showAlert()
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

    private fun showError(editText: EditText, error: String){
        editText.error = error
    }

    private fun showMapActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}