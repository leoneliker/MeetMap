package com.ikalne.meetmap.fragments

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.*
import com.ikalne.meetmap.activities.ChatActivity
import com.ikalne.meetmap.activities.ProfileViewActivity
import com.ikalne.meetmap.models.Chat
import com.ikalne.meetmap.models.PreferencesManager
import com.ikalne.meetmap.models.Suscriber
import java.util.*

class SuscribersAdapter(
    private val suscribersList: MutableList<Suscriber> = mutableListOf(),
) : RecyclerView.Adapter<SuscribersAdapter.SuscriberViewHolder>() {

    class SuscriberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val btnpf: ImageButton = itemView.findViewById(R.id.profile)
        private val btnchat: ImageButton = itemView.findViewById(R.id.chat)
        private var db = Firebase.firestore
        private var user =
            PreferencesManager.getDefaultSharedPreferences(itemView.context).getEmail()
        private var isBtnChatVisible = true
        private val pfImage: ImageView = itemView.findViewById(R.id.pfImage)

        val usersCollection = db.collection("users")

        fun bind(suscriber: Suscriber) {
            nameTextView.text = suscriber.name.toString()
            val userEmail = suscriber.useremail
            val usersCollection = db.collection("users")
            System.out.println("usermail "+userEmail)
            usersCollection.document(userEmail)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Obtener la URL de la imagen del documento
                        val img = documentSnapshot.getString("img")

                        // Cargar la imagen en la ImageView utilizando una biblioteca de carga de imágenes (como Picasso o Glide)
                        // Aquí se utiliza Glide como ejemplo, asegúrate de agregar la dependencia correspondiente en tu archivo build.gradle
                        Glide.with(itemView.context)
                            .load(img)
                            .circleCrop()
                            .into(pfImage)
                    } else {
                        Log.d("SuscribersAdapter", "El documento no existe")
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar la falla de la consulta Firestore
                    Log.e("SuscribersAdapter", "Error al obtener la imagen: $exception")
                }
            btnpf.setOnClickListener {

                openPfpActivity(suscriber.useremail)
            }

            if (getUsernameFromEmail(getUserEmail()) == getUsernameFromEmail(suscriber.useremail)) {
                btnchat.visibility = View.GONE
            } else {
                btnchat.visibility = View.VISIBLE
                btnchat.setOnClickListener {
                    newChat(suscriber.useremail)
                }
            }
        }
        private fun getUserEmail(): String {
            val preferences = PreferencesManager.getDefaultSharedPreferences(itemView.context)
            return preferences.getEmail()
        }

        fun getUsernameFromEmail(email: String): String {
            val index = email.indexOf("@")
            return if (index != -1) {
                email.substring(0, index)
            } else {
                email
            }
        }

        private fun openPfpActivity(userEmail: String) {
            val intent = Intent(itemView.context, ProfileViewActivity::class.java)
            intent.putExtra("userEmail", userEmail)
            itemView.context.startActivity(intent)
        }

        private fun newChat(userEmail: String) {
            System.out.println(userEmail)
            val chatId = UUID.randomUUID().toString()
            val otherUser = userEmail.toString()
            val users_ref = db.collection("users")
            val chatsRef = db.collection("chats")
            val query = users_ref.whereEqualTo("email", otherUser)
            var init = true
            query.get().addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    // Se encontraron documentos que coinciden con la consulta
                    for (document in documents) {
                        System.out.println("el usuario no está en la app")
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
                                                val nameChat = document.getString("name") ?: "Chat"
                                                Log.w("CHATID", "CHATID=  $idChat")
                                                val chat =
                                                    Chat(idChat, nameChat, listOf(user, otherUser))
                                                chatSelected(chat)
                                                break
                                                //ABRIR ESE CHAT????????
                                                //Buscar en el recyclerView ese ID y mostrar solo ese item del recyclerview para que se pueda clicar y abrir
                                                //O abrir directamente ese chat
                                            } else {
                                                init = true
                                            }
                                        }
                                        if (init) {
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

                } else {
                    // No se encontraron documentos que coinciden con la consulta
                    Toast.makeText(itemView.context, "noooo EXISTE EL USUARIO", Toast.LENGTH_LONG)
                        .show()
                }
            }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }
        }

        private fun chatUp(otherUser: String, chatId: String) {
            val users = listOf(user, otherUser)

            val chat = Chat(
                id = chatId,
                name = "Chat con $otherUser",
                users = users
            )

            db.collection("chats").document(chatId).set(chat)
            db.collection("users").document(user).collection("chats").document(chatId).set(chat)
            db.collection("users").document(otherUser).collection("chats").document(chatId)
                .set(chat)

            val intent = Intent(itemView.context, ChatActivity::class.java)
            intent.putExtra("chatId", chatId)
            intent.putExtra("user", user)
            intent.putExtra("name", otherUser)
            itemView.context.startActivity(intent)
        }

        private fun chatSelected(chat: Chat){
            val intent = Intent(itemView.context, ChatActivity::class.java)
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
            itemView.context.startActivity(intent)
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