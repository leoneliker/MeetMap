package com.ikalne.meetmap.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikalne.meetmap.MainAppActivity
import com.ikalne.meetmap.R
import com.ikalne.meetmap.bbddfavlist.Event
import com.ikalne.meetmap.bbddfavlist.EventViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var event: Event
    private lateinit var recyclerView: RecyclerView
    private lateinit var mEventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        recyclerView = findViewById(R.id.recyclerview)
        val adapter = EventListAdapter(EventListAdapter.EventDiff())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener un ViewModel nuevo o existente desde ViewModelProvider.
        mEventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Agregar un observador en el LiveData devuelto por getAllContact.
        // El método onChanged() se ejecuta cuando los datos observados cambian y la actividad está en primer plano.
        mEventViewModel.getAllEvents().observe(this, Observer { events ->
            // Actualizar la lista de contactos en el adaptador.
            adapter.submitList(events)
        })

        adapter.setOnClickListener(View.OnClickListener { v ->
            event = adapter.getEvent(recyclerView.getChildAdapterPosition(v))
            paginaSiguiente(v)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavouritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}