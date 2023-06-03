package com.ikalne.meetmap.model

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.R

class LocationMenuAdapter(private val items: List<LocationMenuItem>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<LocationMenuAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: LocationMenuItem)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.location_menu_item_title)
        val icon: ImageView = itemView.findViewById(R.id.location_menu_item_icon)
        val date: TextView = itemView.findViewById(R.id.location_menu_item_date)
        val time: TextView = itemView.findViewById(R.id.location_menu_item_time)

        @SuppressLint("SetTextI18n")
        fun bind(item: LocationMenuItem, listener: OnItemClickListener) {
            title.text = item.title

            title.setTextColor(ContextCompat.getColor(itemView.context, R.color.secondary_dark)) // Establece el color primario al título
            title.textSize = 16F
            icon.setImageResource(item.icon)
            val dateParts = item.date.split(" ")
            val timeParts = item.time.split(" ")
            try {
                val datePart1 = dateParts[0]
                val datePart2 = timeParts[0]
                val horaFechaIni = dateParts[1].split(":")
                val horaFechaFin = timeParts[1].split(":")
                date.text = "$datePart1 - $datePart2"
                time.text = horaFechaIni[0] + ":" + horaFechaIni[1] + " - " + horaFechaFin[0] + ":" + horaFechaFin[1]
                val layoutParams = icon.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(0, +75, 0, 0)
            } catch (e: Exception) {
                date.text = item.date
                time.text = item.time
            }
            icon.layoutParams.width = 50
            icon.layoutParams.height = 50

            // Agrega el click listener al itemView
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = items.size
}
