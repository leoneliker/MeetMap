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
import androidx.recyclerview.widget.RecyclerView
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

        val adapter = binding.listChatsRecyclerView.adapter as ChatAdapter
        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

               adapter.setData(listChats)
                updateEmptyRecyclerViewVisibility(adapter)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        adapter.setData(listChats)
                        updateEmptyRecyclerViewVisibility(adapter)
                    }
                }

            }
        binding.emptyRecyclerViewImageView.visibility = View.GONE
        binding.emptyRecyclerViewTextView.visibility = View.GONE
        //updateEmptyRecyclerViewVisibility(adapter)
    }

    private fun updateEmptyRecyclerViewVisibility(adapter: ChatAdapter){
        if (adapter.itemCount == 0) {
            binding.emptyRecyclerViewImageView.visibility = View.VISIBLE
            binding.emptyRecyclerViewTextView.visibility = View.VISIBLE
        } else {
            binding.emptyRecyclerViewImageView.visibility = View.GONE
            binding.emptyRecyclerViewTextView.visibility = View.GONE
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
        System.out.println(otherUser)
        val  users_ref= db.collection("users")
        val  chatsRef= db.collection("chats")
        val query= users_ref.whereEqualTo("email",otherUser)
        var init=true
        query.get().addOnSuccessListener { documents ->
            if (documents.size() > 0) {
                // Se encontraron documentos que coinciden con la consulta
                for (document in documents) {
                    Toast.makeText(requireContext(),"EL USUARIO ESTÁ DADO DE ALTA EN LA APP", Toast.LENGTH_LONG).show()
                }
                //QUIERO COMPROBAR SI EL CHAT QUE SE QUIERE HACER YA ESTA ABIERTO
                chatsRef.get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            init = true
                            chatUp(otherUser, chatId)
                        } else {
                            chatsRef.whereArrayContains("users", user).get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        val usersList = document.get("users") as? List<String>
                                        if (usersList != null && usersList.contains(otherUser)) {
                                            init = false
                                            val idChat = document.id
                                            val nameChat=document.getString("name")?: "Chat"
                                            Log.w("CHATID", "CHATID=  $idChat")
                                            val chat = Chat(idChat, nameChat, listOf(user, otherUser))
                                            chatSelected(chat)
                                            break
                                            //ABRIR ESE CHAT????????
                                            //Buscar en el recyclerView ese ID y mostrar solo ese item del recyclerview para que se pueda clicar y abrir
                                            //O abrir directamente ese chat
                                        } else {
                                            init = true
                                        }
                                    }
                                    if(init){
                                        chatUp(otherUser, chatId)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("TAG", "Error al buscar en la lista ", exception)
                                }
                        }


                    }
                    .addOnFailureListener { exception ->
                        Log.w("TAG", "Error al buscar EN CHATS ", exception)
                    }

            }
            else {
                // No se encontraron documentos que coinciden con la consulta
                Toast.makeText(requireContext(),"noooo EXISTE EL USUARIO", Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener {exception ->
    Log.w("TAG", "Error getting documents: ", exception) }
    }

    private fun chatUp(otherUser: String, chatId: String)
    {
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
        intent.putExtra("name", otherUser)
        startActivity(intent)
    }


}


