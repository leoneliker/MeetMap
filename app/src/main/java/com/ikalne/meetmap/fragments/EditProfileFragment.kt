package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikalne.meetmap.Initial
import com.ikalne.meetmap.Login
import com.ikalne.meetmap.PreferencesManager
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

    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container,false)
        initUI()

        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()

        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        binding.mail.setText(email)
        fStore.collection("users").document(email).get().addOnSuccessListener {
            binding.nombre.setText(it.get("name") as String)
            binding.surnombre.setText(it.get("surname") as String)
            binding.phone.setText(it.get("phone") as String)
            binding.description.setText(it.get("description") as String)
            //binding.mail.setText(email)
        }

        return binding.root
    }
    fun initUI()
    {
        binding.btnsave.setOnClickListener{
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
        binding.btnDeleteAccount.setOnClickListener{
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Delete account")
            builder.setMessage("Are you sure you want to delete the account")
            builder.setPositiveButton("Accept") { dialog, which ->
                fStore.collection("users").document(email).delete()
                fAuth.currentUser?.delete()
                Toast.makeText(requireActivity(), "The account has been deleted", Toast.LENGTH_LONG).show()
                PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
                startActivity(Intent(this.requireContext(), Initial::class.java ))
                Intent(binding.root.context, Initial::class.java)
            }
            builder.setNegativeButton("Cancel") { dialog, which ->}
            builder.show()
        }
        binding.btnlogout.setOnClickListener{
            PreferencesManager.getDefaultSharedPreferences(binding.root.context).wipe()
            fAuth.signOut()
            startActivity(Intent(this.requireContext(), Login::class.java ))
            Intent(getActivity(), Login::class.java)

        }
        binding.btncancel.setOnClickListener{
            binding.mail.setText(email)
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