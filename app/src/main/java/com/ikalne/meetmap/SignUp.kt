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

class SignUp : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.repassword)
        val signUp = findViewById<Button>(R.id.btnSignUp)
        val cancel = findViewById<Button>(R.id.btncancel)



        signUp.setOnClickListener{
            signUp()
        }
        cancel.setOnClickListener{
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        }
    }
    fun signUp(){
        if (email.text.isNotEmpty() && password.text.isNotEmpty() && repassword.text.isNotEmpty()){
            if (password.text.toString().equals(repassword.text.toString())){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showMapActivity()
                    }else{
                        showAlert()
                    }
                }
            }else{
                Toast.makeText(this, "Please check both passwords", Toast.LENGTH_LONG).show()
            }
        }else {
           Toast.makeText(this, "Please add your credentials", Toast.LENGTH_LONG).show()
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
    private fun showMapActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}