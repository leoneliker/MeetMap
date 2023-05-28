package com.ikalne.meetmap.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.R
import com.ikalne.meetmap.Suscriber

class SuscribersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SuscribersAdapter
    private var plActId: Int = 0
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suscribers)

        plActId = intent.getIntExtra("plActId", 0)
        userEmail = intent.getStringExtra("UserEmail") ?: ""

        recyclerView = findViewById(R.id.recyclerView)
        adapter = SuscribersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener la lista de suscriptores de Firebase
        updateRecyclerView()

        val buttonBack = findViewById<Button>(R.id.backButton)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun updateRecyclerView() {
        val suscribersRef = FirebaseDatabase.getInstance().getReference("Activities")
            .child(plActId.toString())
            .child("Suscribers")

        suscribersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val suscribersList = mutableListOf<Suscriber>()
                for (suscriberSnapshot in snapshot.children) {
                    val suscriberName = suscriberSnapshot.key
                    val suscriberMail = suscriberSnapshot.value.toString()
                    if (suscriberName != null) {
                        val suscriber = Suscriber(suscriberName, suscriberMail)
                        suscribersList.add(suscriber)
                    }
                }
                adapter.updateList(suscribersList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}