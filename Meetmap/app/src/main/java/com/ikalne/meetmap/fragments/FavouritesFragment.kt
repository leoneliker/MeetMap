package com.ikalne.meetmap.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikalne.meetmap.adapters.AdapterFLI
import com.ikalne.meetmap.R
import com.ikalne.meetmap.databinding.FragmentFavouritesBinding
import com.ikalne.meetmap.models.FLI

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavouritesFragment : Fragment(), AdapterFLI.OnItemClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private var fliArrayList: ArrayList<FLI> = ArrayList()
    private lateinit var binding: FragmentFavouritesBinding
    val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var adapterFLI: AdapterFLI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Crear y configurar el RecyclerView
        recyclerView = binding.favouriteRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterFLI = AdapterFLI(requireContext(), fliArrayList)
        adapterFLI.clickListener = this
        recyclerView.adapter = adapterFLI




        loadFavouriteActs()
        System.out.println("adapter count "+adapterFLI.itemCount)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño para este fragmento
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadFavouriteActs() {
        fliArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!).child("Favourites")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var boolData = false
                    var cnt = 0
                    val data = ArrayList<FLI>()
                    for (ds in snapshot.children) {
                        val aid = "${ds.child("ID").value}"
                        val FLI = FLI()
                        FLI.id = aid
                        data.add(FLI)
                        if(cnt == 0){
                            boolData = true;
                            cnt++;}
                    }
                    System.out.println("boolean "+boolData)
                    if(boolData){
                        binding.emptyRecyclerViewImageView.visibility = View.GONE
                        binding.emptyRecyclerViewTextView.visibility = View.GONE
                    }else{
                        System.out.println(" entra en else")
                        binding.emptyRecyclerViewImageView.visibility = View.VISIBLE
                        binding.emptyRecyclerViewTextView.visibility = View.VISIBLE
                    }
                    updateRecyclerViewData(data)
                    updateRecyclerViewData(data)

                   //updateEmptyRecyclerViewVisibility(adapterFLI)

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        // Mover la inicialización del adaptador aquí
        adapterFLI = AdapterFLI(requireContext(), fliArrayList)
        adapterFLI.clickListener = this
        recyclerView.adapter = adapterFLI

    }

    private fun updateRecyclerViewData(data: ArrayList<FLI>) {
        fliArrayList.clear()
        fliArrayList.addAll(data)
        adapterFLI.notifyDataSetChanged()
    }

    private fun updateEmptyRecyclerViewVisibility(adapter: AdapterFLI) {
        if (adapter.itemCount == 0) {
            binding.emptyRecyclerViewImageView.visibility = View.VISIBLE
            binding.emptyRecyclerViewTextView.visibility = View.VISIBLE
        } else {
            binding.emptyRecyclerViewImageView.visibility = View.GONE
            binding.emptyRecyclerViewTextView.visibility = View.GONE
        }
    }

    override fun onItemClick(position: Int, item: FLI, view: View) {
        val marker = MapFragment.markers["${item.id} ${item.titulo}"]
        if (marker != null) {
            val infoFragment = InfoActivityFragment()
            infoFragment.setMarker(marker, MapFragment.locatorListFav)
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.frame, infoFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
