package com.ikalne.meetmap.activities

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
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
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.ActivityLoginScrollBinding
import com.ikalne.meetmap.forceLightMode
import com.ikalne.meetmap.models.PreferencesManager

class LoginScroll : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var emailTIL: TextInputLayout
    lateinit var passwordTIL: TextInputLayout
    lateinit var resetPass: TextView
    private val GOOGLE_SIGN_IN = 1
    lateinit var fAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginScrollBinding
    private var noInternetDialog: AlertDialog? = null
    private var retryCount = 0

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
        if (isConnectedToInternet()) {
            if (PreferencesManager.getDefaultSharedPreferences(this).getEmail().isNotEmpty())
            {
                retryCount = 0
                showMapActivity()
            }
        } else {
            showNoInternetAlert()
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
                // Obtiene la dirección de correo electrónico del usuario desde la cuenta de Google
                val email = account.email
                // Verifica si el correo electrónico ya está registrado en Firebase
                if (email != null) {
                    fAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Obtiene los métodos de inicio de sesión disponibles para el correo electrónico
                                val signInMethods = task.result?.signInMethods ?: emptyList<String>()
                                if (signInMethods.isEmpty()) {
                                    // El correo electrónico no está registrado en Firebase
                                    Toast.makeText(this, resources.getString(R.string.accountNotExists), Toast.LENGTH_LONG).show()
                                } else {
                                    // El correo electrónico ya está registrado en Firebase
                                    // Continúa con el inicio de sesión
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
                                // Error al verificar el correo electrónico en Firebase
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
        // Ir a una actividad específica
        val intent = Intent(this, Initial::class.java)
        startActivity(intent)
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (!isConnectedToInternet()) {
                showNoInternetAlert()
            } else {
                retryCount = 0
                hideNoInternetAlert()
            }
        }
    }

    private fun hideNoInternetAlert() {
        noInternetDialog?.dismiss()
        noInternetDialog = null
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetAlert() {
        if (noInternetDialog?.isShowing == true) {
            noInternetDialog?.dismiss()
        }

        val builder = AlertDialog.Builder(this)
        // Modificamos el texto del botón de "Reintentar"
        val buttonText = if (retryCount >= 2) {
            builder.setTitle(getString(R.string.nointernetitleexit))
            builder.setMessage(getString(R.string.nointernetsubtitleexit))
            getString(R.string.exitalert)
        } else {
            builder.setTitle(getString(R.string.nointernetitle))
            builder.setMessage(getString(R.string.nointernetsubtitle))
            getString(R.string.nointernetbtn)
        }
        builder.setPositiveButton(buttonText) { dialog, which ->
            if (!isConnectedToInternet()) {
                // Incrementamos el contador de reintentos
                retryCount++
                showNoInternetAlert()
            } else {
                // Reiniciamos el contador de reintentos
                retryCount = 0
                hideNoInternetAlert()
            }
            // Si se ha presionado el botón dos o más veces, cerramos la app
            if (retryCount > 2) {
                hideNoInternetAlert()
                finishAffinity()
            }
        }

        builder.setCancelable(false)
        noInternetDialog = builder.create()
        noInternetDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }
}