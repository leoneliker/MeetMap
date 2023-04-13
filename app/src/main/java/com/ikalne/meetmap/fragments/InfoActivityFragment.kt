package com.ikalne.meetmap.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.databinding.FragmentInfoActivityBinding

class InfoActivityFragment : Fragment() {

    private lateinit var binding: FragmentInfoActivityBinding
    private lateinit var marker: Marker
    private lateinit var locatorsList: List<LocatorView>
    private lateinit var imgbtn: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var plAct: plAct
    private lateinit var email: String
    //private var isGreen = false

    private var IsInMyFavourite = false

    fun setMarker(marker: Marker, locatorsList: List<LocatorView>) {
        this.marker = marker
        this.locatorsList = locatorsList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoActivityBinding.inflate(inflater, container, false)

        val idInfo = MapFragment.madridMap[marker.title]

        /*
        imgbtn = binding.btnedit
        imgbtn.setOnClickListener {
            if (isGreen) {
                imgbtn.setBackgroundResource(R.drawable.love2);
                isGreen = false
            } else {
                imgbtn.setBackgroundResource(R.drawable.love);
                isGreen = true
            }
        }*/
        //init firebase auth


        locatorsList.find { it.id == idInfo }?.let { fillFields(it) }

        firebaseAuth = FirebaseAuth.getInstance()
        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        //System.out.println(plAct.titulo);

        if (firebaseAuth.currentUser != null) {
            checkIsFavourite(plAct)
        }

        binding.btnedit.setOnClickListener() {
            if (firebaseAuth.currentUser == null) {
               // Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show()
            } else {
                if (IsInMyFavourite) {
                    removeFromFavourite(plAct)
                } else {
                    addToFavourite(plAct)
                }
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun fillFields(locatorView: LocatorView) {
        binding.apply {
            titulo.text = locatorView.title

            val desc = if (locatorView.description.isEmpty()) {
                resources.getString(R.string.noHayInfo)
            } else {
                locatorView.description
            }
            description.text = desc

            val date = if (locatorView.dstart.split(" ")[0] == locatorView.dfinish.split(" ")[0]) {
                locatorView.dstart.split(" ")[0]
            } else if (locatorView.dstart.isEmpty() && locatorView.dfinish.isEmpty()) {
                resources.getString(R.string.dateInfo)
            } else {
                locatorView.dstart.split(" ")[0] + " - " + locatorView.dfinish.split(" ")[0]
            }
            fecha.text = date.toString()

            val hora = locatorView.time.ifEmpty {
                resources.getString(R.string.hourInfo)
            }
            horario.text = hora

            val lug = locatorView.event_location.ifEmpty {
                resources.getString(R.string.placeInfo)
            }
            lugar.text = lug
            link.text = resources.getString(R.string.link)

            link.setOnClickListener {
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            info.setOnClickListener {
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
/*
            if (isGreen) {
                val event = Event(
                    id ?: 0,
                    titulo.text.toString(),
                    fecha.text.toString(),
                    horario.text.toString(),
                    lugar.text.toString()
                )
                eventViewModel.insert(event)*/

            plAct = plAct(
                id = Integer.parseInt(locatorView.id),
                titulo = titulo.text.toString(),
                fecha = fecha.text.toString(),
                horario = horario.text.toString(),
                lugar = lugar.text.toString()
            )
                //binding.link.setPaintFlags(binding.link.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
        }

        private fun addToFavourite(plAct: plAct){
            Log.d(TAG, "addToFavourite: Adding to fav")

            val hashMap = HashMap<String, Any>()

            hashMap ["ID"] = plAct.id
            hashMap ["Title"] = plAct.titulo

            hashMap ["Date"] = plAct.fecha
            hashMap ["Time"] = plAct.horario
            hashMap ["Place"] = plAct.lugar

            val ref = FirebaseDatabase.getInstance().getReference("users");
            ref.child(firebaseAuth.uid!!).child("Favourites").child(plAct.id.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG, "addToFavourite: Added to fav")
                }
                .addOnFailureListener{e ->
                    Log.d(TAG, "addToFavourite: Failed to add to fav due to ${e.message}")
                    //Toast.makeText(this, "Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
        private fun removeFromFavourite(plAct: plAct){
            Log.d(TAG, "removeFromFavourite: Removing from fav")

            val ref=FirebaseDatabase.getInstance().getReference("users")
            ref.child(firebaseAuth.uid!!).child("Favourites").child(plAct.id.toString())
                .removeValue()
                .addOnSuccessListener {
                    Log.d(TAG, "removeFromFavourite: removed from fav")
                }
                .addOnFailureListener{e ->
                    Log.d(TAG, "removeFromFavourite: Failed to remove from fav due to ${e.message}")
                   // Toast.makeText(this, "Failed to remove from fav due to ${e.message}", Toast.LENGTH_SHORT).show()

                }
        }

    private fun checkIsFavourite(plAct: plAct) {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("Favourites").child(plAct.id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        IsInMyFavourite = true
                        binding.btnedit.setBackgroundResource(R.drawable.love)
                    } else {
                        IsInMyFavourite = false
                        binding.btnedit.setBackgroundResource(R.drawable.love2)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "checkIsFavourite: onCancelled: ${error.message}")
                    // Toast.makeText(this, "Failed to check favourite status due to ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}




