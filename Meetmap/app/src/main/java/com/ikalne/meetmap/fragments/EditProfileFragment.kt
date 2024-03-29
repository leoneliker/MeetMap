package com.ikalne.meetmap.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.ikalne.meetmap.*
import com.ikalne.meetmap.activities.MainAppActivity
import com.ikalne.meetmap.databinding.FragmentEditProfileBinding
import com.ikalne.meetmap.models.PreferencesManager

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
    companion object{
        const val PRED_URL="https://firebasestorage.googleapis.com/v0/b/meetmap-1856b.appspot.com/o/img%2Fpredeterminado.png?alt=media&token=3bda85a1-f7d2-4bbb-86f1-c96cad24bebd"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        initUI()

        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()
        fStorage = FirebaseStorage.getInstance().getReference()

        img = binding.ivuser

        GALLERY_INTENT = 1

        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        binding.email.setText(email)
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

    fun initUI() {
        binding.btnsave.setOnClickListener {
            saveImg()
            Handler().postDelayed({
                saveImg()
            }, 2000)

            Toast.makeText(requireActivity(), resources.getString(R.string.updatedData), Toast.LENGTH_LONG).show()
            goBack()
        }
        binding.btncancel.setOnClickListener {
            binding.email.setText(email)
            fStore.collection("users").document(email).get().addOnSuccessListener {
                binding.nombre.setText(it.get("name") as String)
                binding.surnombre.setText(it.get("surname") as String)
                binding.phone.setText(it.get("phone") as String)
                binding.description.setText(it.get("description") as String)
                binding.email.setText(email)
            }
            goBack()
        }
        binding.btnedit.setOnClickListener{
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
            saveImg()
        }
    }
    fun saveImg(){

        val storageRef = Firebase.storage.reference.child("img/"+fAuth.currentUser?.email)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            url = uri.toString()
            updateData()
        }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error al descargar la imagen: ${exception.message}")
                url =PRED_URL
                updateData()

            }
    }
//    fun updateimg(){
//        val updates = hashMapOf<String, Any>(
//            "img" to url
//        )
//        fStore.collection("users").document(email).update(updates)
//    }
    fun updateData(){
        val updates = hashMapOf<String, Any>(
            "name" to binding.nombre.text.toString(),
            "surname" to binding.surnombre.text.toString(),
            "phone" to binding.phone.text.toString(),
            "description" to binding.description.text.toString(),
            "img" to url,


        )
        fStore.collection("users").document(email).update(updates)
    }
    fun goBack(){
        startActivity(Intent(this.requireContext(), MainAppActivity::class.java ))
    }
}