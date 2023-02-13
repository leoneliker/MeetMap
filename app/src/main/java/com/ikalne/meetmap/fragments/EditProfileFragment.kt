package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikalne.meetmap.Initial
import com.ikalne.meetmap.Login
import com.bumptech.glide.Glide
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FragmentEditProfileBinding

class EditProfileFragment() : Fragment() {

    lateinit var binding: FragmentEditProfileBinding
    lateinit var fStore: FirebaseFirestore
    lateinit var fAuth: FirebaseAuth
    lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

        //fStore: FirebaseFirestore

    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        initUI()

        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()

        Glide.with(this) //.load("https://images.unsplash.com/photo-1512849934327-1cf5bf8a5ccc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
            .load(R.drawable.cara)
            .circleCrop()
            .into(binding.ivuser)



        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        binding.email.setText(email)
        fStore.collection("users").document(email).get().addOnSuccessListener {
            binding.nombre.setText(it.get("name") as String)
            binding.surnombre.setText(it.get("surname") as String)
            binding.phone.setText(it.get("phone") as String)
            binding.description.setText(it.get("description") as String)
            //binding.mail.setText(email)
        }

        return binding.root
    }

    fun initUI() {
        binding.btnsave.setOnClickListener {
            //binding.mail.setText(email)
            fStore.collection("users").document(email).set(
                hashMapOf(
                    "name" to binding.nombre.text.toString(),
                    "surname" to binding.surnombre.text.toString(),
                    "phone" to binding.phone.text.toString(),
                    "description" to binding.description.text.toString(),
                )
            )
            //fAuth.currentUser?.updatePassword(binding.password.text.toString())
            Toast.makeText(requireActivity(), "Updated data", Toast.LENGTH_LONG).show()
        }
        binding.btncancel.setOnClickListener {
            binding.email.setText(email)
            fStore.collection("users").document(email).get().addOnSuccessListener {
                binding.nombre.setText(it.get("name") as String)
                binding.surnombre.setText(it.get("surname") as String)
                binding.phone.setText(it.get("phone") as String)
                binding.description.setText(it.get("description") as String)
                //binding.mail.setText(email)
            }
        }
    }
}