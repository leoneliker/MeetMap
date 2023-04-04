package com.ikalne.meetmap.adapters

import android.R.attr.data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.databinding.ItemChatBinding
import com.ikalne.meetmap.models.Chat


class ChatAdapter(val chatClick: (Chat) -> Unit): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private var user = ""
    private var chatid= ""
    private var db = Firebase.firestore


    var chats: List<Chat> = emptyList()

    fun setData(list: List<Chat>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        user= PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
        return ChatViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding.chatNameText.text = chats[position].name
        holder.binding.usersTextView.text = chats[position].users.toString()

        holder.itemView.setOnClickListener {
            chatClick(chats[position])
        }

       /* holder.itemView.setOnLongClickListener {
            chatid=chats[holder.adapterPosition].id
            removeItem(holder.adapterPosition)
            true
        }*/
    }

    override fun getItemCount(): Int {
        return chats.size
    }

   /* fun removeItem(position: Int) {

        chats = chats.filterIndexed { index, _ -> index != position }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, chats.size)

        val db = Firebase.firestore
        val userRef = db.collection("users").document(user)
        val chatRef = userRef.collection("chats").document(chatid)
        chatRef.delete()
    }*/

    class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)
}

