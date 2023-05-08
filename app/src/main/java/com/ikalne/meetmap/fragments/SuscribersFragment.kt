package com.ikalne.meetmap.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.R
import com.ikalne.meetmap.Suscriber

class SuscribersFragment(private val plActId: Int) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SuscribersAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_suscribers, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = SuscribersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Obtener la lista de suscriptores de Firebase
        updateRecyclerView()

        return view
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
                    if (suscriberName != null) {
                        val suscriber = Suscriber(suscriberName)
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