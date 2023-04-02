package com.ikalne.meetmap.bbddfavlist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.R

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameItemView: TextView = itemView.findViewById(R.id.name)
    private val dateItemView: TextView = itemView.findViewById(R.id.date)
    private val timeItemView: TextView = itemView.findViewById(R.id.time)
    private val placeItemView: TextView = itemView.findViewById(R.id.place)

    fun bind(name: String?, date: String?, time: String?, place: String?) {
        nameItemView.text = name
        dateItemView.text = date
        timeItemView.text = time
        placeItemView.text = place
    }

    companion object {
        fun create(view: View): ContactViewHolder {
            return ContactViewHolder(view)
        }
    }
}