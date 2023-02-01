package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.repassword)
        val signup = findViewById<Button>(R.id.btnSignUp)
        val cancel = findViewById<Button>(R.id.btncancel)
        signup.setOnClickListener{signUp()}
        cancel.setOnClickListener{
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        }
    }



    fun signUp(){
        val expRegular = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,15}\$")
        val emailTIL = findViewById<TextInputLayout>(R.id.etemail)
        val passwordTIL = findViewById<TextInputLayout>(R.id.etpassword)
        val repasswordTIL = findViewById<TextInputLayout>(R.id.etrepassword)
        if (email.text.isEmpty() && password.text.isEmpty() && repassword.text.isEmpty()){
            showError(emailTIL, "This field can´t be empty")
            showError(passwordTIL, "This field can´t be empty")
            showError(repasswordTIL, "This field can´t be empty")
        }else if (!email.text.contains("@")){
            showError(emailTIL, "Email is not valid.")  //This field can´t be empty
        }else if(!expRegular.matches(password.text.toString())){
            showError(passwordTIL, "Password needs: Capital letters, small letters, numbers and must be longer than 6 characters")
        }else if (!password.text.toString().equals(repassword.text.toString())){
            showError(repasswordTIL, "Passwords must me the same")
        }else{
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        PreferencesManager.getDefaultSharedPreferences(this).saveEmail(email.text.toString())
                        PreferencesManager.getDefaultSharedPreferences(this).savePass(password.text.toString())
                        PreferencesManager.getDefaultSharedPreferences(this).saveRePass(repassword.text.toString())
                        val intent = Intent(this, MainAppActivity::class.java)
                        startActivity(intent)
                    }else{
                        //showAlert()
                        showError(emailTIL, "This email already exists")
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