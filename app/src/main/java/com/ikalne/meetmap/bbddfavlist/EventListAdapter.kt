package com.ikalne.meetmap.bbddfavlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.R

class ContactListAdapter(private val listener: View.OnClickListener) :
    ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favlist_item, parent, false)
        view.setOnClickListener(listener)
        return EventViewHolder.create(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.event_name, current.event_date, current.event_time, current.event_place)
    }

    fun getEvent(pos: Int): Event {
        return getItem(pos)
    }
}