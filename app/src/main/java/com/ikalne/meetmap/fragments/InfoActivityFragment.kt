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
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.databinding.FragmentInfoActivityBinding

class InfoActivityFragment :Fragment() {

    lateinit var imgLayout: ImageView
    private lateinit var binding : FragmentInfoActivityBinding
    private lateinit var marker: Marker
    private lateinit var locatorsList: List<LocatorView>

    private lateinit var imgbtn: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var plAct: plAct
    private lateinit var email: String
    private var IsInMyFavourite = false
    private var IsInSuscribe = false

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
        locatorsList.find { it.id==idInfo }?.let { fillFields(it) }
        firebaseAuth = FirebaseAuth.getInstance()
        email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        if (firebaseAuth.currentUser != null) {
            checkIsFavourite(plAct)
        }

        binding.btnedit.setOnClickListener() {
            checkIsFavourite(plAct)
            if (firebaseAuth.currentUser == null) {
                // Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show()
            } else {
                if (IsInMyFavourite) {
                    removeFromFavourite(plAct)
                    checkIsFavourite(plAct)
                } else {
                    addToFavourite(plAct)
                    checkIsFavourite(plAct)
                }
            }

        }

        checkIsSuscribe(plAct, email)
        binding.unirse.setOnClickListener() {
            if (firebaseAuth.currentUser == null) {
                // Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show()

            } else {
                if (IsInSuscribe) {
                    removeFromSuscribe(plAct, email)
                    checkIsSuscribe(plAct, email)

                } else {
                    addToSuscribe(plAct, email)
                    checkIsSuscribe(plAct, email)
                    openSuscribersFragment(plAct.id,email)
                }

                // Cargar el fragmento de suscriptores


            }
        }
        locatorsList.find { it.id==idInfo }?.let { fillFields(it) }
        return binding.root
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun fillFields(locatorView: LocatorView) {
        binding.apply {
            imgLayout = binding.ivevent
            val options = listOf(
                resources.getDrawable(R.drawable.amigos, null),
                resources.getDrawable(R.drawable.amigoscielo, null)
            )
            Log.i("categoria",locatorView.category)
            val res = when (locatorView.category.split("/").getOrNull(6) ?: options.random()) {
                "Musica" -> resources.getDrawable(R.drawable.musica, null)
                "DanzaBaile" -> resources.getDrawable(R.drawable.danzabaile, null)
                "CursosTalleres" -> resources.getDrawable(R.drawable.cursotalleres, null)
                "TeatroPerformance" -> resources.getDrawable(R.drawable.teatro, null)
                "ActividadesCalleArteUrbano" -> resources.getDrawable(R.drawable.arteurbano, null)
                "CuentacuentosTiteresMarionetas" -> resources.getDrawable(R.drawable.cuentacuentos, null)
                //"ProgramacionDestacadaAgendaCultura" -> resources.getDrawable(R.drawable.destacada, null)
                "ComemoracionesHomenajes" -> resources.getDrawable(R.drawable.homenajes, null)
                "ConferenciasColoquios" -> resources.getDrawable(R.drawable.conferencias, null)
                "1ciudad21distritos" -> resources.getDrawable(R.drawable.distritos, null)
                "ExcursionesItinerariosVisitas" -> resources.getDrawable(R.drawable.excursiones, null)
                "ItinerariosOtrasActividadesAmbientales" -> resources.getDrawable(R.drawable.ambientales, null)
                "ClubesLectura" -> resources.getDrawable(R.drawable.lectura, null)
                "RecitalesPresentacionesActosLiterarios" -> resources.getDrawable(R.drawable.recitales, null)
                "Exposiciones" -> resources.getDrawable(R.drawable.exposiciones, null)
                "Campamentos" -> resources.getDrawable(R.drawable.campamentos, null)
                "CineActividadesAudiovisuales" -> resources.getDrawable(R.drawable.cine, null)
                "CircoMagia" -> resources.getDrawable(R.drawable.circo, null)
                "FiestasSemanaSanta" -> resources.getDrawable(R.drawable.semanasanta, null)

                else -> options.random()
            }
            activity?.let { Glide.with(it)
                .load(res)
                .into(imgLayout)}


            titulo.text = locatorView.title
            val desc = locatorView.description.ifEmpty {
                resources.getString(R.string.noHayInfo)
            }
            description.text = desc

            val date = if (locatorView.dstart.split(" ")[0]==locatorView.dfinish.split(" ")[0]){
                locatorView.dstart.split(" ")[0]
            }else if(locatorView.dstart.isEmpty() && locatorView.dfinish.isEmpty()){
                resources.getString(R.string.dateInfo)
            }
            else{
                locatorView.dstart.split(" ")[0]+" - "+locatorView.dfinish.split(" ")[0]
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
            //binding.link.setPaintFlags(binding.link.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            link.setOnClickListener{
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            info.setOnClickListener{
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            plAct = plAct(
                id = Integer.parseInt(locatorView.id),
                titulo = titulo.text.toString(),
                fecha = fecha.text.toString(),
                horario = horario.text.toString(),
                lugar = lugar.text.toString()
            )
        }
    }
    private fun addToFavourite(plAct: plAct){
        Log.d(TAG, "addToFavourite: Adding to fav")
        IsInMyFavourite = true
        checkIsFavourite(plAct)
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
        IsInMyFavourite = false
        checkIsFavourite(plAct)
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
                        binding.btnedit.setBackgroundResource(R.drawable.love2)
                    } else {
                        IsInMyFavourite = false
                        binding.btnedit.setBackgroundResource(R.drawable.love)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "checkIsFavourite: onCancelled: ${error.message}")
                    // Toast.makeText(this, "Failed to check favourite status due to ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun checkIsSuscribe(plAct: plAct, userEmail: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Activities")
        ref.child(plAct.id.toString()).child("Suscribers").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                val usernameFromEmail = getUsernameFromEmail(userEmail)
                var isInSubscribe = false
                snapshot.children.forEach { childSnapshot ->
                    val username = childSnapshot.key
                    if (username == usernameFromEmail) {
                        isInSubscribe = true
                        return@forEach
                    }
                }
                IsInSuscribe = isInSubscribe
                if (isInSubscribe) {
                    Log.d(TAG, "checkIsSub: dentro")
                    binding.unirse.setBackgroundColor(R.color.secondary_dark)
                    binding.unirse.setTextColor(R.color.dark_gray)
                    binding.unirse.text = "Unsubscribe"
                } else {
                    Log.d(TAG, "checkIsSub: fuera")
                    binding.unirse.setBackgroundColor(R.color.secondary_light)
                    binding.unirse.setTextColor(R.color.light_gray)
                    binding.unirse.text = "Suscribe"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "checkIsFavourite: onCancelled: ${error.message}")
                // Toast.makeText(this, "Failed to check favourite status due to ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToSuscribe(plAct: plAct, userEmail: String){
        Log.d(TAG, "addToSuscribe: Adding to suscribe")
        checkIsSuscribe(plAct, userEmail)
        IsInSuscribe = true

        val ref = FirebaseDatabase.getInstance().getReference("Activities")
        ref.child(plAct.id.toString()).child("Suscribers")
            .child(getUsernameFromEmail(userEmail))
            .setValue("true")
            .addOnSuccessListener {
                Log.d(TAG, "addToSuscribe: Added to suscribe")
            }
            .addOnFailureListener{e ->
                Log.d(TAG, "addToSuscribe: Failed to add to suscribe due to ${e.message}")
                //Toast.makeText(this, "Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromSuscribe(plAct: plAct, userEmail: String){
        Log.d(TAG, "removeFromSuscribe: Removing from suscribe")
        checkIsSuscribe(plAct, userEmail)
        IsInSuscribe = false

        val ref = FirebaseDatabase.getInstance().getReference("Activities")
        ref.child(plAct.id.toString()).child("Suscribers")
            .child(getUsernameFromEmail(userEmail))
            .removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "removeFromSuscribe: removed from suscribe")
            }
            .addOnFailureListener{e ->
                Log.d(TAG, "removeFromSuscribe: Failed to remove from suscribe due to ${e.message}")
                // Toast.makeText(this, "Failed to remove from fav due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    fun getUsernameFromEmail(email: String): String {
        val index = email.indexOf("@")
        return if (index != -1) {
            email.substring(0, index)
        } else {
            email
        }
    }

    fun openSuscribersFragment(plActId: Int, UserEmail: String) {
        val SuscribersFragment = SuscribersFragment(plActId, UserEmail)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, SuscribersFragment)
            .addToBackStack(null)
            .commit()
    }

}




