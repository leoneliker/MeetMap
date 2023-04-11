package com.ikalne.meetmap.bbddfavlist

import androidx.recyclerview.widget.DiffUtil

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.event_name == newItem.event_name && oldItem.event_date == newItem.event_date && oldItem.event_place == newItem.event_place && oldItem.event_time == newItem.event_time
    }
}
