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
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.R
import com.ikalne.meetmap.Suscriber
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.databinding.FragmentInfoActivityBinding
import org.w3c.dom.Text
import java.lang.Integer.parseInt
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.Delegates
import kotlin.random.Random

class InfoActivityFragment :Fragment() {

    lateinit var imgLayout: ImageView
    private lateinit var binding : FragmentInfoActivityBinding
    private lateinit var marker: Marker
    private lateinit var locatorsList: List<LocatorView>

    private lateinit var imgbtn: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var plAct: plAct
    private lateinit var email: String
    private lateinit var bubble_people: TextView
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
        bubble_people = binding.bubblePeople
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
        //getImageFromFirestore(plAct.id)
        NumberSubs(plAct.id) { numberSubs ->
            // Aquí puedes hacer uso del número de suscriptores devuelto
            bubble_people.text ="+$numberSubs"
            if(bubble_people.text.equals("+0")){
                bubble_people.text = " "
            }
            // Hacer cualquier otra acción que necesites con el número de suscriptores
        }

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




            }
        }

        binding.bubbles.setOnClickListener {
            System.out.println("click en el linear")
            openSuscribersFragment(plAct.id, email)
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
                "Exposiciones" -> resources.getDrawable(R.drawable.exposiciones, null)
                "Campamentos" -> resources.getDrawable(R.drawable.campamentos, null)
                "CineActividadesAudiovisuales" -> resources.getDrawable(R.drawable.cine, null)
                "CircoMagia" -> resources.getDrawable(R.drawable.circo, null)
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
                    binding.bubbles.visibility = View.VISIBLE
                } else {
                    Log.d(TAG, "checkIsSub: fuera")
                    binding.unirse.setBackgroundColor(R.color.secondary_light)
                    binding.unirse.setTextColor(R.color.light_gray)
                    binding.unirse.text = "Suscribe"
                    binding.bubbles.visibility = View.GONE
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
            .setValue(userEmail)
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
        System.out.println(UserEmail)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, SuscribersFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun NumberSubs(plActId: Int, callback: (Int) -> Unit) {
        val suscribersRef = FirebaseDatabase.getInstance().getReference("Activities")
            .child(plActId.toString())
            .child("Suscribers")
        System.out.println("dentro de number")
        var numberSubs: Int = 0
        suscribersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val suscribersList = mutableListOf<Suscriber>()
                for (suscriberSnapshot in snapshot.children) {
                    val suscriberName = suscriberSnapshot.key
                    if (suscriberName != null) {
                        System.out.println("cuenta")
                        numberSubs += 1
                    }
                }
                System.out.println("cuenta fuera"+numberSubs)
                callback(numberSubs) // Llamar a la devolución de llamada con el número de suscriptores
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
    /*
    private fun RandomSub(plActId: Int, callback: (String) -> Unit) {
        var number: Int = 0
        var cont: Int = 0
        var callbackEmail: String = ""

        val suscribersRef = FirebaseDatabase.getInstance().getReference("Activities")
            .child(plActId.toString())
            .child("Suscribers")

        System.out.println("dentro de randomsub")

        NumberSubs(plActId) { numberSubs ->
            // Aquí puedes hacer uso del número de suscriptores devuelto
            number = numberSubs
            System.out.println(number)

            suscribersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val suscribersList = mutableListOf<Suscriber>()
                    for (suscriberSnapshot in snapshot.children) {
                        val suscriberName = suscriberSnapshot.key
                        val suscriberEmail = suscriberSnapshot.value

                        if (suscriberName != null) {
                            val randomNumber = Random.nextInt(1,number)
                            if (cont == randomNumber) {
                                callbackEmail = suscriberEmail.toString()
                            }
                            cont++
                        }
                    }
                    System.out.println("callbackemail"+callbackEmail+"")
                    callback(callbackEmail) // Llamar a la devolución de llamada con el número de suscriptores
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    private fun getImageFromFirestore(plActId: Int){
        val db = FirebaseFirestore.getInstance()
        RandomSub(plActId) { RandomSub ->
            val collectionPath = "users/"
            db.collection(collectionPath).document(RandomSub)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val imageUrl = document.getString("img")
                        val bubbleImage = binding.bubbleImage
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(bubbleImage)
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar el error de Firebase Firestore
                }
        }
    }

    // Uso del método para obtener una imagen de Firestore
    /*val email = "correo_electronico_del_suscriptor_random"
    getImageFromFirestore(email) { imageUrl ->
        if (imageUrl != null) {
            // Aquí puedes utilizar la URL de la imagen obtenida
            // Para cargar la imagen en un ImageView, puedes usar una biblioteca de terceros como Glide o Picasso
            // Ejemplo con Glide:
            Glide.with(requireContext())
                .load(imageUrl)
                .into(imgLayout)
        } else {
            // No se encontró la imagen en Firebase Firestore
        }
    }*/
*/
}





