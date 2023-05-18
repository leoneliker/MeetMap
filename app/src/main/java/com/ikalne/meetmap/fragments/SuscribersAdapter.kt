package com.ikalne.meetmap.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.ikalne.meetmap.ProfileViewFragment
import com.ikalne.meetmap.R
import com.ikalne.meetmap.Suscriber

class SuscribersAdapter(
    private val suscribersList: MutableList<Suscriber> = mutableListOf(),
) : RecyclerView.Adapter<SuscribersAdapter.SuscriberViewHolder>() {

    class SuscriberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val btnpf: ImageButton = itemView.findViewById(R.id.profile)
        private val btnchat: ImageButton = itemView.findViewById(R.id.chat)

        fun bind(suscriber: Suscriber) {
            nameTextView.text = suscriber.name.toString()

            btnpf.setOnClickListener {
                openPfpFragment(suscriber.useremail)
            }
        }

        private fun openPfpFragment(userEmail: String) {
            val fragmentManager: FragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
            val fragment: Fragment = ProfileViewFragment(userEmail)
            fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuscriberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_suscriber, parent, false)
        return SuscriberViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuscriberViewHolder, position: Int) {
        val suscriber = suscribersList[position]
        holder.bind(suscriber)
    }

    override fun getItemCount(): Int {
        return suscribersList.size
    }

    fun updateList(newList: List<Suscriber>) {
        suscribersList.clear()
        suscribersList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addSuscriber(suscriber: Suscriber) {
        suscribersList.add(suscriber)
        notifyItemInserted(suscribersList.size - 1)
    }
}