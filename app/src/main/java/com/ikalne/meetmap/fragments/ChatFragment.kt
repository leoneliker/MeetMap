package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.ChatActivity
import com.ikalne.meetmap.PreferencesManager
import com.ikalne.meetmap.R
import com.ikalne.meetmap.adapters.ChatAdapter
import com.ikalne.meetmap.databinding.FragmentChatBinding
import com.ikalne.meetmap.models.Chat
import java.util.*


class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private var user = ""
    private var db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }

    fun initUI() {

        user= PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()

        if (user.isNotEmpty()){
            initViews()
        }
    }


    private fun initViews(){
        binding.newChatButton.setOnClickListener { newChat() }

        binding.listChatsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", user)
        if(user==chat.users[0])
        {
            intent.putExtra("name", chat.users[1])
        }
        else if(user==chat.users[1])
        {
            intent.putExtra("name", chat.users[0])
        }
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = binding.newChatText.text.toString()
        val  users_ref= db.collection("users")
        val query= users_ref.whereEqualTo("email",otherUser)
        query.get().addOnSuccessListener { documents ->
            if (documents.size() > 0) {
                // Se encontraron documentos que coinciden con la consulta
                for (document in documents) {
                    Toast.makeText(requireContext(),"YA EXISTE EL USUARIO", Toast.LENGTH_LONG).show()
                }
            } else {
                // No se encontraron documentos que coinciden con la consulta
                Toast.makeText(requireContext(),"noooo EXISTE EL USUARIO", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { exception ->
            Log.w("TAG", "Error getting documents: ", exception)
        }
        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("users").document(user).collection("chats").document(chatId).set(chat)
        db.collection("users").document(otherUser).collection("chats").document(chatId).set(chat)

        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user)
        intent.putExtra("name", "Chat con $otherUser")
        startActivity(intent)
    }


}
