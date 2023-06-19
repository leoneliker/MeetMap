package com.ikalne.meetmap

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.databinding.ActivityChatBinding
import com.ikalne.meetmap.databinding.ActivityProfileviewBinding

class ProfileViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileviewBinding
    private var toolbar: Toolbar? = null
    private lateinit var etName: TextView
    private lateinit var etSurname: TextView
    private lateinit var etPhone: TextView
    private lateinit var etEmail: TextView
    private lateinit var etDescription: TextView
    private lateinit var etPfppic: ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileview)
        binding = ActivityProfileviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        etName = findViewById(R.id.nombre)
        etSurname = findViewById(R.id.surnombre)
        etPhone = findViewById(R.id.phone)
        etEmail = findViewById(R.id.email)
        etDescription = findViewById(R.id.description)
        etPfppic = findViewById(R.id.ivuser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val useremail = intent.getStringExtra("userEmail")
        System.out.println("email de aqui "+useremail)
        useremail?.let { getUserData(it) }
    }

    private fun getUserData(useremail: String) {
        Log.d(TAG, "getUserData: $useremail")
        val documentRef = Firebase.firestore.collection("users").document(useremail)

        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                Log.d(TAG, "getUserData: DocumentSnapshot exists")

                val name = documentSnapshot.getString("name")
                val surname = documentSnapshot.getString("surname")
                val numTelf = documentSnapshot.getString("phone")
                val desc = documentSnapshot.getString("description")
                val photo = documentSnapshot.getString("img")

                name?.let { etName.text = it }
                surname?.let { etSurname.text = it }
                numTelf?.let { etPhone.text = it }
                desc?.let { etDescription.text = it }
                etEmail.text = useremail
                binding.toolbar.title = getString(R.string.profile_of)+ " "+ getUsernameFromEmail(useremail)


                photo?.let {
                    val options = RequestOptions().placeholder(R.drawable.predeterminado)
                        .error(R.drawable.predeterminado)
                    Glide.with(this)
                        .load(it)
                        .apply(options)
                        .circleCrop()
                        .into(etPfppic)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error reading user data: ${exception.message}")
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getUsernameFromEmail(email: String): String {
        val index = email.indexOf("@")
        return if (index != -1) {
            email.substring(0, index)
        } else {
            email
        }
    }

    companion object {
        private const val TAG = "ProfileViewActivity"
    }
}