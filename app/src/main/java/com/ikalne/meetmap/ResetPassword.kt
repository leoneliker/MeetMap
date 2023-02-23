package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var emailTIL: TextInputLayout
    lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        forceLightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        email = findViewById(R.id.email)
        fAuth = FirebaseAuth.getInstance()
        emailTIL = findViewById(R.id.etemail)

        val resetPass = findViewById<Button>(R.id.btnreset)
        val cancel = findViewById<Button>(R.id.btncancel)

        cancel.setOnClickListener{
            val intent = Intent(this, LoginScroll::class.java)
            startActivity(intent)
        }
        resetPass.setOnClickListener {resetPassword()}
    }

    fun resetPassword(){
        if (email.text.isEmpty()){
            showError(emailTIL, resources.getString(R.string.emailRequired))
        }else if(!email.text.contains("@")){
            showError(emailTIL, resources.getString(R.string.emailValid))
        }else{
            fAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener(
                OnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, resources.getString(R.string.checkMail), Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, resources.getString(R.string.tryAgain), Toast.LENGTH_LONG).show()
                    }


                })
        }
    }
    private fun showError(textInputLayout: TextInputLayout, error: String){
        textInputLayout.error = error
    }
}