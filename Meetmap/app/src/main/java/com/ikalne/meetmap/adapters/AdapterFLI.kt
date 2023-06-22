package com.ikalne.meetmap.adapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FavlistItemBinding
import com.ikalne.meetmap.models.FLI
import java.util.*
import kotlin.collections.ArrayList

class AdapterFLI(private val context: Context, private var fliArrayList: ArrayList<FLI>) :
    RecyclerView.Adapter<AdapterFLI.HolderFLI>() {

    private lateinit var binding: FavlistItemBinding


    var clickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int, item: FLI, itemView: View)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFLI {
        binding = FavlistItemBinding.inflate(LayoutInflater.from(context),parent, false)

        return HolderFLI(binding.root)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: HolderFLI, position: Int) {
        val model = fliArrayList[position]
        loadActDetails(model, holder)
        holder.itemView.setOnClickListener {
            clickListener?.onItemClick(position,model, holder.itemView)
        }
        holder.removeFavBtn.setOnClickListener{
            removeFromFavourite(context, model.id)
        }

    }

    private fun removeFromFavourite(context: Context, ActID: String){
        Log.d(ContentValues.TAG, "removeFromFavourite: Removing from fav")
        val firebaseAuth = FirebaseAuth.getInstance()
        val ref=FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("Favourites").child(ActID)
            .removeValue()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "removeFromFavourite: removed from fav")
            }
            .addOnFailureListener{e ->
                Log.d(ContentValues.TAG, "removeFromFavourite: Failed to remove from fav due to ${e.message}")
                // Toast.makeText(this, "Failed to remove from fav due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun loadActDetails(model: FLI, holder: HolderFLI) {
        val ActID = model.id
        var time = ""
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let {
            val ref = FirebaseDatabase.getInstance().getReference("users/$it/Favourites")
            ref.child(ActID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val title = "${snapshot.child("Title").value}"
                    val date = "${snapshot.child("Date").value}"
                    if("${snapshot.child("Time").value}".equals("No hay hora establecida")){
                        time = ""
                    }else{
                        time = "${snapshot.child("Time").value}"
                    }
                    val place = "${snapshot.child("Place").value}"
                    val cat = "${snapshot.child("Cat").value}" // Agregado: Obtener el valor de "Cat"

                    model.isFav = true
                    model.titulo = title
                    model.lugar = place
                    model.fecha = date
                    model.horario = time
                    model.id = ActID
                    holder.fli_title.text = title
                    holder.fli_desc.text = place
                    holder.fli_date.text = date
                    holder.fli_time.text = time

                    // Agregado: Cargar la foto si "cat" es igual a "Exposicion"
                    if (cat == "Musica") {
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_musica)
                    }else if (cat == "DanzaBaile"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_danzabaile)
                    }else if (cat == "CursosTalleres"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_cursostalleres)
                    }else if (cat == "TeatroPerformance"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_teatro)
                    }else if (cat == "ActividadesCalleArteUrbano"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_arteurbano)
                    }else if (cat == "CuentacuentosTiteresMarionetas"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_cuentacuentos)
                    }else if (cat == "ConferenciasColoquios"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_conferencias)
                    }else if (cat == "1ciudad21distritos"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_ciudaddistritos)
                    }else if (cat == "ExcursionesItinerariosVisitas"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_campamentos)
                    }else if (cat == "ItinerariosOtrasActividadesAmbientales"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_ambientales)
                    }else if (cat == "ClubesLectura"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_lectura)
                    }else if (cat == "RecitalesPresentacionesActosLiterarios"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_recitales)
                    }else if (cat == "Exposiciones"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_exposiciones)
                    }else if (cat == "Campamentos"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_campamentos)
                    }else if (cat == "CineActividadesAudiovisuales"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_cine)
                    }else if (cat == "CircoMagia"){
                        holder.fli_cat.setBackgroundResource(R.drawable.ico_circo)
                    }else{
                        holder.fli_cat.setBackgroundResource(R.color.white)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error de base de datos si es necesario
                }
            })
        }
    }


    override fun getItemCount(): Int {
        return fliArrayList.size
    }

    inner class HolderFLI(itemView: View): RecyclerView.ViewHolder(itemView){
        var fli_title = binding.fliTitle
        var fli_cat = binding.catIV
        var fli_desc = binding.fliDesc
        var fli_time = binding.fliTime
        var fli_date = binding.fliDate
        var removeFavBtn = binding.removeFavBtn
    }

}