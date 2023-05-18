package com.ikalne.meetmap

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.ikalne.meetmap.model.User

class ProfileViewFragment(private val useremail: String) : Fragment() {
    private lateinit var etName: TextView
    private lateinit var etSurname: TextView
    private lateinit var etPhone: TextView
    private lateinit var etEmail: TextView
    private lateinit var etDescription: TextView
    private lateinit var etPfppic: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profileview, container, false)

        etName = view.findViewById(R.id.nombre)
        etSurname = view.findViewById(R.id.surnombre)
        etPhone = view.findViewById(R.id.phone)
        etEmail = view.findViewById(R.id.email)
        etDescription = view.findViewById(R.id.description)
        etPfppic = view.findViewById(R.id.ivuser)

        // Obtener los datos del usuario desde Firebase según su correo electrónico
        System.out.println(useremail)
        getUserData(useremail)
        System.out.println(etEmail)

        return view
    }

    private fun getUserData(useremail: String) {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("users").document(useremail)

        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    if (user != null) {
                        System.out.println(user.email)
                    }
                    user?.let {
                        etName.text = it.name
                        etSurname.text = it.surname
                        etPhone.text = it.numTelf.toString()
                        etEmail.text = it.email
                        etDescription.text = it.desc

                        // Cargar la imagen del perfil desde la URL proporcionada
                        val options = RequestOptions().placeholder(R.drawable.predeterminado)
                            .error(R.drawable.predeterminado)
                        Glide.with(requireContext())
                            .load(it.photo)
                            .apply(options)
                            .into(etPfppic)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error reading user data: ${exception.message}")
            }
    }

    companion object {
        private const val TAG = "ProfileViewFragment"

        fun newInstance(useremail: String): ProfileViewFragment {
            return ProfileViewFragment(useremail)
        }
    }
}