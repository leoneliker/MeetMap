package com.ikalne.meetmap

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ikalne.meetmap.databinding.ActivitySignUpScrollBinding

class SignUpScroll : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText
    lateinit var emailTIL: TextInputLayout
    lateinit var passwordTIL: TextInputLayout
    lateinit var repasswordTIL: TextInputLayout
    lateinit var fStore: FirebaseFirestore
    lateinit var fAuth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 1
    private lateinit var binding: ActivitySignUpScrollBinding
    private var url= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        forceLightMode()
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        if(PreferencesManager.getDefaultSharedPreferences(this).getEmail().isNotEmpty())
        {
            showMapActivity()
        }
    }

    fun signUp(){
        val expRegular = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,15}\$")

        if (email.text.isEmpty() && password.text.isEmpty() && repassword.text.isEmpty()){
            showError(emailTIL, resources.getString(R.string.emailRequired))
            showError(passwordTIL, resources.getString(R.string.passRequired))
            showError(repasswordTIL, resources.getString(R.string.repassRequired))
        }else if (!email.text.contains("@")){
            showError(emailTIL, resources.getString(R.string.emailValid))  //This field can´t be empty
        }else if(!expRegular.matches(password.text.toString())){
            showError(passwordTIL,  resources.getString(R.string.passNeeds))
        }else if (!password.text.toString().equals(repassword.text.toString())){
            showError(repasswordTIL, resources.getString(R.string.passSame))
        }else{
            fAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    fAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, resources.getString(R.string.verifyEmail), Toast.LENGTH_LONG).show()
                    }
                    PreferencesManager.getDefaultSharedPreferences(this).saveEmail(email.text.toString())
                    val storageRef = Firebase.storage.reference.child("img/predeterminado.png")
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        url = uri.toString()
                        imgNormal()
                    }

                }else{
                    showError(emailTIL,  resources.getString(R.string.emailExists))
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            fAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(this,  resources.getString(R.string.verifyEmail), Toast.LENGTH_LONG).show()
                            }
                            fAuth.currentUser?.email?.let { it1 -> PreferencesManager.getDefaultSharedPreferences(this).saveEmail(it1) }
                            val storageRef = Firebase.storage.reference.child("img/predeterminado.png")
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                url = uri.toString()
                                imgGoogle()
                            }
                        }else{
                            showError(emailTIL,  resources.getString(R.string.emailExists))
                        }
                    }
                }
            }catch (e: ApiException){
                Log.w("ERROR", " " + e)
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
    private fun imgGoogle(){
        fAuth.currentUser?.email?.let { it1 ->
            fStore.collection("users").document(it1).set(
                hashMapOf(
                    "name" to "",
                    "email" to PreferencesManager.getDefaultSharedPreferences(this).getEmail(),
                    "surname" to "",
                    "phone" to "",
                    "description" to "",
                    "img" to url,

                    )
            )
        }
        showMapActivity()
    }
    private fun imgNormal(){
        fStore.collection("users").document(email.text.toString()).set(
            hashMapOf(
                "name" to "",
                "email" to PreferencesManager.getDefaultSharedPreferences(this).getEmail(),
                "surname" to "",
                "phone" to "",
                "description" to "",
                "img" to url,
                )
        )
        showMapActivity()
    }
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (!isConnectedToInternet()) {
                showNoInternetAlert()
            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.nointernetitle))
        builder.setMessage(getString(R.string.nointernetsubtitle))
        builder.setPositiveButton(getString(R.string.nointernetbtn)) { dialog, which ->
            if (!isConnectedToInternet()) {
                showNoInternetAlert()
            }
        }
        builder.setCancelable(false)
        builder.show()
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