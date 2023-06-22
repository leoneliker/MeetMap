package com.ikalne.meetmap.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.R
import com.ikalne.meetmap.models.Suscriber
import com.ikalne.meetmap.databinding.ActivitySuscribersBinding


class SuscribersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SuscribersAdapter
    lateinit var binding: ActivitySuscribersBinding
    private var toolbar: Toolbar? = null
    private var plActId: Int = 0
    private lateinit var userEmail: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suscribers)
        binding = ActivitySuscribersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        plActId = intent.getIntExtra("plActId", 0)
        userEmail = intent.getStringExtra("UserEmail") ?: ""
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = SuscribersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val customFont = ResourcesCompat.getFont(this, R.font.concert_one)
        binding.toolbar.title = getString(R.string.meetmappers)
        binding.toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyleMeetMappers)
        binding.toolbar.getChildAt(0)?.let { toolbarTitle ->
            if (toolbarTitle is TextView) {
                toolbarTitle.typeface = customFont
            }
        }
        // Obtener la lista de suscriptores de Firebase
        updateRecyclerView()
        /*val buttonBack = findViewById<Button>(R.id.backButton)
        buttonBack.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.button_dark))
        buttonBack.setOnClickListener {
            finish()
        }*/
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