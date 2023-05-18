package com.ikalne.meetmap.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.*
import com.ikalne.meetmap.databinding.ActivityChatBinding
import com.ikalne.meetmap.databinding.ActivityListOfChatsBinding
import com.ikalne.meetmap.databinding.ActivityMainAppBinding
import com.ikalne.meetmap.models.Chat
import java.util.*

class SuscribersAdapter(
    private val suscribersList: MutableList<Suscriber> = mutableListOf(),
) : RecyclerView.Adapter<SuscribersAdapter.SuscriberViewHolder>() {

    class SuscriberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val btnpf: ImageButton = itemView.findViewById(R.id.profile)
        private val btnchat: ImageButton = itemView.findViewById(R.id.chat)
        private var db = Firebase.firestore
        private var user = PreferencesManager.getDefaultSharedPreferences(itemView.context).getEmail()

        fun bind(suscriber: Suscriber) {
            nameTextView.text = suscriber.name.toString()

            btnpf.setOnClickListener {
                openPfpFragment(suscriber.useremail)
            }

            btnchat.setOnClickListener {
                newChat(suscriber.useremail)
            }
        }

        private fun openPfpFragment(userEmail: String) {
            val fragmentManager: FragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
            val fragment: Fragment = ProfileViewFragment(userEmail)
            fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit()
        }

        private fun newChat(userEmail: String) {
            val chatId = UUID.randomUUID().toString()
            val otherUser = userEmail
            val users = listOf(user, userEmail)

            val chat = Chat(
                id = chatId,
                name = "Chat con $userEmail",
                users = users
            )

            db.collection("chats").document(chatId).set(chat)
                .addOnSuccessListener {
                    db.collection("users").document(user).collection("chats").document(chatId).set(chat)
                        .addOnSuccessListener {
                            db.collection("users").document(otherUser).collection("chats").document(chatId).set(chat)
                                .addOnSuccessListener {
                                    val intent = Intent(itemView.context, ChatActivity::class.java)
                                    intent.putExtra("chatId", chatId)
                                    intent.putExtra("user", user)
                                    itemView.context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    // Handle error
                                }
                        }
                        .addOnFailureListener { e ->
                            // Handle error
                        }
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuscriberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_suscriber, parent, false)
        return SuscriberViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuscriberViewHolder, position: Int) {
        val suscriber = suscribersList[position]
        holder.bind(suscriber)
    }

    override fun getItemCount(): Int {
        return suscribersList.size
    }

    fun updateList(newList: List<Suscriber>) {
        suscribersList.clear()
        suscribersList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addSuscriber(suscriber: Suscriber) {
        suscribersList.add(suscriber)
        notifyItemInserted(suscribersList.size - 1)
    }
}