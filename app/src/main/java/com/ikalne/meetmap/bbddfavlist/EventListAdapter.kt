package com.ikalne.meetmap.bbddfavlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.R

class ContactListAdapter(private val listener: View.OnClickListener) :
    ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        view.setOnClickListener(listener)
        return ContactViewHolder.create(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.mName, current.mPhone)
    }

    fun getContacto(pos: Int): Event {
        return getItem(pos)
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            fun create(parent: ViewGroup): EventViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return EventViewHolder(view)
            }
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.ev_id == newItem.ev_id
        }
    }
}