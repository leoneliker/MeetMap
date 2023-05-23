package com.ikalne.meetmap.adapters

import android.R.attr.data
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.ItemChatBinding
import com.ikalne.meetmap.models.Chat
import com.ikalne.meetmap.models.Message


class ChatAdapter(private val context: Context, val chatClick: (Chat) -> Unit): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
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

        val currentUser = PreferencesManager.getDefaultSharedPreferences(holder.itemView.context).getEmail()
        val chat = chats[position]
        val chatId = chat.id
        val messagesRef = db.collection("chats").document(chatId).collection("messages")
        val otherUser = if (chat.users[0] == currentUser) chat.users[1] else chat.users[0]
        holder.binding.chatNameText.text = otherUser.substringBefore("@")
        messagesRef.orderBy("dob", Query.Direction.DESCENDING).limit(1)
            .addSnapshotListener { querySnapshot, exception ->
                if(exception != null){
                    holder.binding.usersTextView.text = "Error al obtener mensajes"
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val layoutParams = holder.binding.chatmessage.layoutParams as ConstraintLayout.LayoutParams
                    val textLayoutParams = holder.binding.usersTextView.layoutParams as ConstraintLayout.LayoutParams
                    val lastMessage = querySnapshot.documents[0].toObject(Message::class.java)
                    val lastMessageText = lastMessage?.message
                    val lastMessageSender = lastMessage?.from
                    if (currentUser == lastMessageSender){
                        holder.binding.chatmessage.visibility = View.GONE
                        layoutParams.marginStart = 0.dpToPixels(context)
                        textLayoutParams.marginStart = 32.dpToPixels(context)
                    }
                    else{
                        holder.binding.chatmessage.visibility = View.VISIBLE
                        layoutParams.marginStart = 32.dpToPixels(context)
                        textLayoutParams.startToEnd = R.id.chatmessage
                    }
                    holder.binding.chatmessage.layoutParams = layoutParams
                    //val lastMessageSenderName = lastMessageSender?.substringBefore("@")
                    //val lastMessageDisplay = "$lastMessageSenderName: $lastMessageText"
                    // Actualiza el TextView con el último mensaje
                    holder.binding.usersTextView.layoutParams = textLayoutParams
                    holder.binding.usersTextView.text = lastMessageText
                    //holder.binding.usersTextView.text = chats[position].users.toString()
                } else {
                    // No se encontraron mensajes en el chat
                    holder.binding.usersTextView.text = "No hay mensajes"
                }
            }

        db.collection("users").document(otherUser).get().addOnSuccessListener {
            Glide.with(holder.itemView.context)
                .load(it.get("img")as String)
                .circleCrop()
                .into(holder.binding.chatImage)
        }

        db.collection("users").document(otherUser).addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Actualiza la vista con el nuevo valor del nombre
                Glide.with(holder.itemView.context)
                    .load(documentSnapshot.getString("img"))
                    .circleCrop()
                    .into(holder.binding.chatImage)
            }
        }

        holder.itemView.setOnClickListener {
            chatClick(chats[position])
        }

        holder.itemView.setOnLongClickListener {
            chatid=chats[holder.adapterPosition].id
           // removeItem(holder.adapterPosition)
            true
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

   /*fun removeItem(position: Int) {

        chats = chats.filterIndexed { index, _ -> index != position }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, chats.size)

        val db = Firebase.firestore
        val userRef = db.collection("users").document(user)
        val chatRef = userRef.collection("chats").document(chatid)
        chatRef.delete()
    }*/
    fun getItemPosition(chatId: String): Int {
        return chats.indexOfFirst { chat -> chat.id == chatId }
    }
    fun filterChatById(chatId: String) {
        chats = chats.filter { it.id == chatId }
        notifyDataSetChanged()
    }
    private fun Int.dpToPixels(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    fun Int.pixelsToDp(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (this / scale + 0.5f).toInt()
    }

    class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)
}

