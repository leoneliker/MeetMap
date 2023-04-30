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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ikalne.meetmap.R
import com.ikalne.meetmap.Suscriber

class SuscribersFragment(private val plActId: Int) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SuscribersAdapter
    private lateinit var plAct: plAct

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
        val suscribersRef = Firebase.firestore
            .collection("Activities")
            .document(plActId.toString())
            .collection("Suscribers")
        suscribersRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            val suscribersList = mutableListOf<Suscriber>()
            for (doc in snapshot!!.documents) {
                val suscriber = doc.toObject<Suscriber>()
                suscriber?.let { suscribersList.add(it) }
            }
            adapter.updateList(suscribersList)
        }
        return view
    }
}