package com.ikalne.meetmap.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.model.CustomInfoWindowAdapter
import com.ikalne.meetmap.viewmodels.MadridViewModel
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ikalne.meetmap.model.LocationMenuAdapter
import com.ikalne.meetmap.model.LocationMenuItem


class MapFragment : Fragment(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{

    private lateinit var map: GoogleMap
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var dimView: View
    private var locatorList = listOf<LocatorView>()
    private val viewModel: MadridViewModel by lazy {
        ViewModelProvider(this)[MadridViewModel::class.java]
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        const val REQUEST_CODE_LOCATION = 0
        val madridMap = hashMapOf<String, String>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_map, container, false).apply {
        dimView = findViewById(R.id.dim_view)
        dimView.setOnClickListener(null)
        dimView.visibility = View.VISIBLE
        loadingSpinner = findViewById(R.id.loading_spinner)
        loadingSpinner.visibility = View.VISIBLE
        observe()
        val mMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mMapFragment.getMapAsync(this@MapFragment)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun observe() {
        viewModel.locators.observe(viewLifecycleOwner) { locators ->
            locators.mapNotNull {
                it.location.latitude?.let { lat ->
                    it.location.longitude?.let { lng ->
                        LatLng(lat, lng)
                    }
                }?.let { coordinates ->
                    MarkerOptions().position(coordinates)
                        .title(it.id + " " + it.title).also { marker ->
                            madridMap[marker.title] = it.id
                            map.addMarker(marker).setIcon(
                                BitmapDescriptorFactory.fromResource(R.drawable.mano_rosa)
                            )
                        }
                }
            }.also {
                locatorList = locators
                map.setInfoWindowAdapter(
                    CustomInfoWindowAdapter(
                        LayoutInflater.from(activity),
                        locatorList
                    )
                )
                map.setOnInfoWindowClickListener(this@MapFragment)
                loadingSpinner.visibility = View.GONE
                dimView.visibility = View.GONE
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableLocation()
        viewModel.fetchData()
        map.setOnMarkerClickListener { false }
        map.setOnMyLocationClickListener(this)
    }

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        } else {
            requestLocationPermission()
        }
    }



    override fun onMyLocationButtonClick() = false

    override fun onInfoWindowClick(marker: Marker) {
        val infoFragment = InfoActivityFragment()
        infoFragment.setMarker(marker, locatorList)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, infoFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(activity, R.string.location, Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onMyLocationClick(location: Location) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.location_menu, null)
        bottomSheetDialog.setContentView(view)

        val menuItems = mutableListOf<LocationMenuItem>()
        for (locator in locatorList) {
            val distance = distance(
                location.latitude, location.longitude,
                locator.location.latitude ?: 0.0, locator.location.longitude ?: 0.0
            )
            if (distance < 500) {
                val menuItem = LocationMenuItem(locator.title, R.drawable.ic_calendar, locator.dstart, locator.dfinish)
                menuItems.add(menuItem)
            }
        }
        if (menuItems.isEmpty()){
            menuItems.add(LocationMenuItem("No se han encontrado actividades cercanas", R.drawable.ic_baseline_cancel_24, "", ""))
        }
        val adapter = LocationMenuAdapter(menuItems)
        view.findViewById<RecyclerView>(R.id.location_menu_recycler_view).apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.location_menu_height)
        bottomSheetDialog.show()
    }

    //Codigo para conseguir pasar distancias de long y lat a metros
    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val R = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return (R * c * 1000).toFloat()
    }


}