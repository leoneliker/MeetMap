package com.ikalne.meetmap

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var emailTIL: TextInputLayout
    lateinit var passwordTIL: TextInputLayout
    lateinit var resetPass: TextView
    private val GOOGLE_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        emailTIL = findViewById(R.id.etemail)
        passwordTIL = findViewById(R.id.etpassword)
        resetPass = findViewById(R.id.tvresetpass)

        val login = findViewById<Button>(R.id.btnlogin)
        val cancel = findViewById<Button>(R.id.btncancel)
        val btngoogle = findViewById<ImageButton>(R.id.btngoogle)

        login.setOnClickListener { login() }
        btngoogle.setOnClickListener { loginGoogle() }
        cancel.setOnClickListener{
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        }
        resetPass.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }
        checkUserValues()
    }

    fun checkUserValues()
    {
        if (PreferencesManager.getDefaultSharedPreferences(this).getEmail().isNotEmpty())
        {
            showMapActivity()
        }
    }

    fun login(){
        if (email.text.isEmpty() && password.text.isEmpty()){
            showError(emailTIL, "Email is required")
            showError(passwordTIL, "Password is required")
        }else if(!email.text.contains("@")){
            showError(emailTIL, "Email is not valid")
        }else{
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        PreferencesManager.getDefaultSharedPreferences(this).saveEmail(email.text.toString())
                        PreferencesManager.getDefaultSharedPreferences(this).savePass(password.text.toString())
                        showMapActivity()
                    }else{
                        //showAlert()
                        showError(emailTIL, "Incorrect email or password")
                    }
                }
        }
    }

    fun loginGoogle(){
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("570907010994-lq2g37kb3kop7inhocsuft9gpgcd0ofu.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
//        googleClient.signInIntent.also {
//            startActivityForResult(it, GOOGLE_SIGN_IN)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Toast.makeText(this, "pasa", Toast.LENGTH_LONG).show()
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
//                            MeetMapApplication.prefs.saveEmail(email.text.toString())
//                            MeetMapApplication.prefs.savePass(password.text.toString())
                            showMapActivity()
                        } else {
                            //showAlert()
                            showError(emailTIL, "Incorrect email or password")
                        }
                    }
                }
            }catch (e: ApiException){
                //showAlert(e)
                Log.w("CAGOENTODO", " " + e)
            }

        }
    }

    private fun showAlert(e: ApiException){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(e.toString())
        //builder.setMessage("Se ha producido un error de autenticación al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showError(textInputLayout: TextInputLayout, error: String){
        textInputLayout.error = error
    }

    private fun showMapActivity(){
        val intent = Intent(this, MainAppActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        // Ir a una actividad específica
        val intent = Intent(this, Initial::class.java)
        startActivity(intent)
    }
}