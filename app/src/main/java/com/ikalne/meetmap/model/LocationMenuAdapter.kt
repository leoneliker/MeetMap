package com.ikalne.meetmap.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.R

class LocationMenuAdapter(private val items: List<LocationMenuItem>) :
    RecyclerView.Adapter<LocationMenuAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.location_menu_item_title)
        val icon: ImageView = itemView.findViewById(R.id.location_menu_item_icon)
        val date: TextView = itemView.findViewById(R.id.location_menu_item_date)
        val time: TextView = itemView.findViewById(R.id.location_menu_item_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.icon.setImageResource(item.icon)
        holder.date.text = item.date
        holder.time.text = item.time
    }

    override fun getItemCount(): Int = items.size
}

