package com.ikalne.meetmap.bbddfavlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.R

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameItemView: TextView = itemView.findViewById(R.id.nameView)
    private val dateItemView: TextView = itemView.findViewById(R.id.dateView)
    private val timeItemView: TextView = itemView.findViewById(R.id.timeView)
    private val placeItemView: TextView = itemView.findViewById(R.id.placeView)

    fun bind(name: String?, date: String?, time: String?, place: String?) {
        nameItemView.text = name
        dateItemView.text = date
        timeItemView.text = time
        placeItemView.text = place
    }

    companion object {
        fun create(parent: ViewGroup): EventViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.favlist_item, parent, false)
            return EventViewHolder(view)
        }
    }
}