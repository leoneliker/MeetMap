package com.ikalne.meetmap

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.databinding.FavlistItemBinding
import com.ikalne.meetmap.databinding.FragmentFavouritesBinding
import com.ikalne.meetmap.fragments.plAct
import com.ikalne.meetmap.model.FLI

class AdapterFLI: RecyclerView.Adapter<AdapterFLI.HolderFLI> {

    private val context: Context
    private var fliArrayList: ArrayList<FLI>

    private lateinit var binding: FavlistItemBinding

    constructor(context: Context, fliArrayList: ArrayList<FLI>) {
        this.context = context
        this.fliArrayList = fliArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFLI {
        binding = FavlistItemBinding.inflate(LayoutInflater.from(context),parent, false)

        return HolderFLI(binding.root)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: HolderFLI, position: Int) {
        val model = fliArrayList[position]

            loadActDetails(model, holder)

            holder.itemView.setOnClickListener{
                //Intent a la actividad
            }

        holder.removeFavBtn.setOnClickListener{
            removeFromFavourite(context, model.id)
        }

    }

    private fun removeFromFavourite(context: Context, ActID: String){
        val TAG="REMOVE_FAV_TAG"
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

    private fun loadActDetails(model: FLI, holder: AdapterFLI.HolderFLI) {
        val ActID = model.id

        val ref = FirebaseDatabase.getInstance().getReference("users/favourites")
        ref.child(ActID)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val title = "${snapshot.child("Title").value}"
                    val date = "${snapshot.child("Date").value}"
                    val time = "${snapshot.child("Time").value}"
                    val place = "${snapshot.child("Place").value}"

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
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return fliArrayList.size
    }

    inner class HolderFLI(itemView: View): RecyclerView.ViewHolder(itemView){
        var fli_title = binding.fliTitle
        var removeFavBtn = binding.removeFavBtn
        var fli_desc = binding.fliDesc
        var fli_time = binding.fliTime
        var fli_date = binding.fliDate
    }

}