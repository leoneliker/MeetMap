package com.ikalne.meetmap

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ikalne.meetmap.databinding.ActivityLoginScrollBinding

class LoginScroll : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var emailTIL: TextInputLayout
    lateinit var passwordTIL: TextInputLayout
    lateinit var resetPass: TextView
    private val GOOGLE_SIGN_IN = 1
    lateinit var fAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginScrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        forceLightMode()
        super.onCreate(savedInstanceState)

        binding = ActivityLoginScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        emailTIL = findViewById(R.id.etemail)
        passwordTIL = findViewById(R.id.etpassword)
        resetPass = findViewById(R.id.tvresetpass)
        fAuth = FirebaseAuth.getInstance()

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
            showError(emailTIL, resources.getString(R.string.emailRequired))
            showError(passwordTIL, resources.getString(R.string.passRequired))
        }else if(!email.text.contains("@")){
            showError(emailTIL, resources.getString(R.string.emailValid))
        }else{
            fAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        PreferencesManager.getDefaultSharedPreferences(this).saveEmail(email.text.toString())
                        PreferencesManager.getDefaultSharedPreferences(this).savePass(password.text.toString())
                        showMapActivity()
                    }else{
                        //showAlert()
                        showError(emailTIL, resources.getString(R.string.incorrectEmailPass))
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Obtiene la direcci??n de correo electr??nico del usuario desde la cuenta de Google
                val email = account.email
                // Verifica si el correo electr??nico ya est?? registrado en Firebase
                if (email != null) {
                    fAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Obtiene los m??todos de inicio de sesi??n disponibles para el correo electr??nico
                                val signInMethods = task.result?.signInMethods ?: emptyList<String>()
                                if (signInMethods.isEmpty()) {
                                    // El correo electr??nico no est?? registrado en Firebase
                                    Toast.makeText(this, resources.getString(R.string.accountNotExists), Toast.LENGTH_LONG).show()
                                } else {
                                    // El correo electr??nico ya est?? registrado en Firebase
                                    // Contin??a con el inicio de sesi??n
                                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                                    fAuth.signInWithCredential(credential)
                                        .addOnCompleteListener { authTask ->
                                            if (authTask.isSuccessful) {
                                                fAuth.currentUser?.email?.let { PreferencesManager.getDefaultSharedPreferences(this).saveEmail(it) }
                                                showMapActivity()
                                            }
                                        }
                                }
                            } else {
                                // Error al verificar el correo electr??nico en Firebase
                                Log.w(TAG, "Error fetching sign-in methods for email", task.exception)
                            }
                        }
                }

            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun showError(textInputLayout: TextInputLayout, error: String){
        textInputLayout.error = error
    }

    private fun showMapActivity(){
        val intent = Intent(this, MainAppActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Ir a una actividad espec??fica
        val intent = Intent(this, Initial::class.java)
        startActivity(intent)
    }
}