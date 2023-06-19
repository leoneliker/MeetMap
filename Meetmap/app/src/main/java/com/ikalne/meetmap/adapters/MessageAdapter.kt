package com.ikalne.meetmap.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.models.Message
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.ItemMessageBinding


class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageAdapter.MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if(user == message.from){
            holder.binding.myMessageLayout.visibility = View.VISIBLE
            holder.binding.otherMessageLayout.visibility = View.GONE

            holder.binding.myMessageTextView.text = message.message
        } else {
            holder.binding.myMessageLayout.visibility = View.GONE
            holder.binding.otherMessageLayout.visibility = View.VISIBLE

            holder.binding.othersMessageTextView.text = message.message
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root)
}