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
    ListAdapter<Event,EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favlist_item, parent, false)
        view.setOnClickListener(listener)
        return EventViewHolder.create(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.mName, current.mPhone)
    }

    fun getContacto(pos: Int): Event {
        return getItem(pos)
    }

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

    class ContactDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.event_name == newItem.event_name && oldItem.event_date == newItem.event_date && oldItem.event_place == newItem.event_place && oldItem.event_time == newItem.event_time;
        }
    }
}