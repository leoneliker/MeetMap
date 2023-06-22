package com.ikalne.meetmap.activities
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikalne.meetmap.adapters.MessageAdapter
import com.ikalne.meetmap.models.Message
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    private var toolbar: Toolbar? = null
    private var chatId = ""
    private var user = ""
    private var name = ""
    private var noInternetDialog: AlertDialog? = null
    private var retryCount = 0


    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }

        name = intent.getStringExtra("name").toString()

        // Ahora puedes usar la variable name como una cadena en tu actividad
        Log.d("TAG", "El nombre es $name")



        if (isConnectedToInternet()) {
            if(chatId.isNotEmpty() && user.isNotEmpty()) {
                retryCount = 0
                initViews()
            }
        } else {
            showNoInternetAlert()
        }
    }

    private fun initViews(){
        binding.messagesRecylerView.layoutManager = LinearLayoutManager(this)
        binding.messagesRecylerView.adapter = MessageAdapter(user)
        val text= name.substringBefore("@")
        binding.toolbar.title = getString(R.string.title_chat)+ " $text"
        binding.sendMessageButton.setOnClickListener { sendMessage() }


        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendMessage(){
        val message = Message(
            message = binding.messageTextField.text.toString(),
            from = user
        )

        db.collection("chats").document(chatId).collection("messages").document().set(message)

        binding.messageTextField.setText("")


    }
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (!isConnectedToInternet()) {
                showNoInternetAlert()
            } else {
                hideNoInternetAlert()
            }
        }
    }

    private fun hideNoInternetAlert() {
        noInternetDialog?.dismiss()
        noInternetDialog = null
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showNoInternetAlert() {
        if (noInternetDialog?.isShowing == true) {
            noInternetDialog?.dismiss()
        }

        val builder = AlertDialog.Builder(this)
        // Modificamos el texto del botón de "Reintentar"
        val buttonText = if (retryCount >= 2) {
            builder.setTitle(getString(R.string.nointernetitleexit))
            builder.setMessage(getString(R.string.nointernetsubtitleexit))
            getString(R.string.exitalert)
        } else {
            builder.setTitle(getString(R.string.nointernetitle))
            builder.setMessage(getString(R.string.nointernetsubtitle))
            getString(R.string.nointernetbtn)
        }
        builder.setPositiveButton(buttonText) { dialog, which ->
            if (!isConnectedToInternet()) {
                // Incrementamos el contador de reintentos
                retryCount++
                showNoInternetAlert()
            } else {
                // Reiniciamos el contador de reintentos
                retryCount = 0
                hideNoInternetAlert()
            }
            // Si se ha presionado el botón dos o más veces, cerramos la app
            if (retryCount > 2) {
                hideNoInternetAlert()
                finishAffinity()
            }
        }

        builder.setCancelable(false)
        noInternetDialog = builder.create()
        noInternetDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }
}