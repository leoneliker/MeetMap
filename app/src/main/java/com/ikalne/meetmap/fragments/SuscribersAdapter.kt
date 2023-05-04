package com.ikalne.meetmap.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.ikalne.meetmap.R
import com.ikalne.meetmap.Suscriber

class SuscribersAdapter(private val suscribersList: MutableList<Suscriber> = mutableListOf()) :
    RecyclerView.Adapter<SuscribersAdapter.SuscriberViewHolder>() {

    class SuscriberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(suscriber: Suscriber) {
            nameTextView.text = suscriber.name.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuscriberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_suscriber, parent, false)
        return SuscriberViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuscriberViewHolder, position: Int) {
        holder.bind(suscribersList[position])
    }

    override fun getItemCount(): Int {
        return suscribersList.size
    }

    fun updateList(newList: List<Suscriber>) {
        suscribersList.clear()
        suscribersList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addSuscriber(doc: DocumentSnapshot) {
        val suscriber = Suscriber(doc.id)
        suscribersList.add(suscriber)
        notifyItemInserted(suscribersList.size - 1)
    }
}