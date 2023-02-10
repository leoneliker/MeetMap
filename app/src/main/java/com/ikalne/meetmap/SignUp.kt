package com.ikalne.meetmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.material.textfield.TextInputEditText
import com.ikalne.meetmap.MeetMapApplication.Companion.prefs
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText
    lateinit var emailTIL: TextInputLayout
    lateinit var passwordTIL: TextInputLayout
    lateinit var repasswordTIL: TextInputLayout
    lateinit var fStore: FirebaseFirestore
    lateinit var fAuth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.repassword)
        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()
        emailTIL = findViewById(R.id.etemail)
        passwordTIL = findViewById(R.id.etpassword)
        repasswordTIL = findViewById(R.id.etrepassword)

        val signup = findViewById<Button>(R.id.btnSignUp)
        val cancel = findViewById<Button>(R.id.btncancel)
        val btngoogle = findViewById<ImageButton>(R.id.btngoogle)

        signup.setOnClickListener{signUp()}
        btngoogle.setOnClickListener { signUpGoogle() }
        cancel.setOnClickListener{
            val intent = Intent(this, Initial::class.java)
            startActivity(intent)
        }
        checkUserValues()
    }

    fun checkUserValues()
    {
        if(prefs.getEmail().isNotEmpty())
        {
            showMapActivity()
        }
    }

    fun signUp(){
        val expRegular = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,15}\$")

        if (email.text.isEmpty() && password.text.isEmpty() && repassword.text.isEmpty()){
            showError(emailTIL, "Email is required")
            showError(passwordTIL, "Password is required")
            showError(repasswordTIL, "Repeat password is required")
        }else if (!email.text.contains("@")){
            showError(emailTIL, "Email is not valid.")  //This field can´t be empty
        }else if(!expRegular.matches(password.text.toString())){
            showError(passwordTIL, "Password needs: Capital letters, small letters, numbers and must be longer than 6 characters")
        }else if (!password.text.toString().equals(repassword.text.toString())){
            showError(repasswordTIL, "Passwords must me the same")
        }else{
            fAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        fAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(this, "Please verify your email", Toast.LENGTH_LONG).show()
                        }
                        prefs.saveEmail(email.text.toString())
//                        prefs.savePass(password.text.toString())
//                        prefs.saveRePass(repassword.text.toString())
                        fStore.collection("users").document(email.text.toString()).set(
                            hashMapOf(
                                "name" to "",
                                "surname" to "",
                                "phone" to "",
                                "description" to "",
                            )
                        )
                        showMapActivity()
                    }else{
                        //showAlert()
                        showError(emailTIL, "This email already exists")
                    }
                }
        }
    }

    fun signUpGoogle(){
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
                        if (it.isSuccessful){
                            fAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(this, "Please verify your email", Toast.LENGTH_LONG).show()
                                //Log.w("QUE", ""+fAuth.currentUser?.email +" -> com.google.firebase.auth.internal.zzx@b5b75a1")
                            }
                            fAuth.currentUser?.email?.let { it1 -> prefs.saveEmail(it1) }
                            fAuth.currentUser?.email?.let { it1 ->
                                fStore.collection("users").document(it1).set(
                                    hashMapOf(
                                        "name" to "",
                                        "surname" to "",
                                        "phone" to "",
                                        "description" to "",
                                    )
                                )
                            }
                            showMapActivity()
                        }else{
                            //showAlert()
                            showError(emailTIL, "This email already exists")
                        }
                    }
                }
            }catch (e: ApiException){
                //showAlert(e)
                Log.w("ERROR", " " + e)
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
        val intent = Intent(this, MainAppActivity::class.java)
        startActivity(intent)
    }
}