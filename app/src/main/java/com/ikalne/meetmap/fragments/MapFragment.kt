package com.ikalne.meetmap.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.model.CustomInfoWindowAdapter
import com.ikalne.meetmap.viewmodels.MadridViewModel


class MapFragment : Fragment(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {
    private lateinit var map: GoogleMap
    private var locatorList = listOf<LocatorView>()
    private val viewModel: MadridViewModel by lazy {
        ViewModelProvider(this)[MadridViewModel::class.java]
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
        val madridMap = hashMapOf<String, String>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        observe()
        val v: View = inflater.inflate(R.layout.fragment_map, container, false)
        requestLocationPermission()
        val mMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mMapFragment.getMapAsync(this)
        return v
    }

    fun observe() {
        viewModel.locators.observe(viewLifecycleOwner) { locators ->
            locators.forEach {
                val coordinates = it.location.latitude?.let { it1 ->
                    it.location.longitude?.let { it2 ->
                        LatLng(
                            it1,
                            it2
                        )
                    }
                }
                val marker = coordinates?.let { it1 ->
                    MarkerOptions().position(it1)
                        .title(it.id + " " + it.title)
                }
                marker?.let { it1 -> it.id.let { it2 -> madridMap.put(it1.title, it2) } }

                marker?.let { map.addMarker(it) }
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mano_rosa))
            }

            locatorList = locators
            map.setInfoWindowAdapter(CustomInfoWindowAdapter(LayoutInflater.from(activity),locatorList));
            map.setOnInfoWindowClickListener(this)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewModel.fetchData()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()

        val Madrid = LatLng(40.401490, -3.708010)
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(Madrid, 14f)
        )

        map.setOnMarkerClickListener {// on marker click we are getting the title of our marker
            false
        }




    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                activity,
                resources.getString(R.string.location),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {

        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
    }

    override fun onInfoWindowClick(marker: Marker) {


        val infoFragment = InfoActivityFragment()
        infoFragment.setMarker(marker,locatorList)

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame, infoFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }


}