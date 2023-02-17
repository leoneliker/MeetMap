package com.ikalne.meetmap.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.ikalne.meetmap.Initial
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.databinding.FragmentEditProfileBinding

class EditProfileFragment() : Fragment() {

    lateinit var binding: FragmentEditProfileBinding
    lateinit var fStore: FirebaseFirestore
    lateinit var fAuth: FirebaseAuth
    lateinit var fStorage: StorageReference
    lateinit var email: String
    lateinit var GALLERY_INTENT: Number
    lateinit var uri: Uri
    lateinit var filePath: StorageReference
    lateinit var img: ImageView
    private var url= ""

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
        fStorage = FirebaseStorage.getInstance().getReference()

        img = binding.ivuser

        GALLERY_INTENT = 1

        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        Log.w("QUE-EMAIL", ""+email)
        binding.mail.setText(email)
        fStore.collection("users").document(email).get().addOnSuccessListener {
            binding.nombre.setText(it.get("name") as String)
            binding.surnombre.setText(it.get("surname") as String)
            binding.phone.setText(it.get("phone") as String)
            binding.description.setText(it.get("description") as String)
            //binding.mail.setText(email)
            Glide.with(this)
                .load(it.get("img")as String)
                .circleCrop()
                .into(img)
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
                    "img" to uri,
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
                fStorage.child("img").child(email).delete()
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
            startActivity(Intent(this.requireContext(), Initial::class.java ))
            Intent(getActivity(), Initial::class.java)

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
        binding.btnedit.setOnClickListener{
            //binding.ivuser
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY_INTENT as Int)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            if (data != null) {
                uri = data.data!!
            }
            filePath = fAuth.currentUser?.email.let { fStorage.child("img").child(it.toString()) }
            filePath.putFile(uri)
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(img)
            val storageRef = Firebase.storage.reference.child("img/"+fAuth.currentUser?.email)
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                url = uri.toString()
                updateimg()
            }
        }
    }

    fun updateimg(){
        val updates = hashMapOf<String, Any>(
            "img" to url
        )
        fStore.collection("users").document(email).update(updates)
    }
}