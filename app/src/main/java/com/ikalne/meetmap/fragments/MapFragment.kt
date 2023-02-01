package com.ikalne.meetmap.fragments

import android.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ikalne.meetmap.MapsActivity


class MapFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }
    var Madrid = LatLng(40.401490, -3.708010)
    var Charla_Ted = LatLng(40.403940, -3.707746)
    var Poesia = LatLng(40.403351, -3.701352)
    var Hockey = LatLng(40.399824, -3.703590)
    var Teatro = LatLng(40.401490, -3.708010)

    var latLngArrayList: ArrayList<LatLng> = ArrayList()
    var locationNameArraylist: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(com.ikalne.meetmap.R.layout.fragment_map, container, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        latLngArrayList!!.add(Charla_Ted);
        locationNameArraylist!!.add("Charla TED");
        latLngArrayList!!.add(Poesia);
        locationNameArraylist!!.add("Recital de poesia");
        latLngArrayList!!.add(Hockey);
        locationNameArraylist!!.add("Partido de hockey");
        latLngArrayList!!.add(Teatro);
        locationNameArraylist!!.add("Teatro: La maravilla de tu madre");

        requestLocationPermission()
        val mMapFragment = childFragmentManager.findFragmentById(com.ikalne.meetmap.R.id.map) as SupportMapFragment
        mMapFragment.getMapAsync(this)
        return v
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        for (i in 0 until latLngArrayList.size) {
            map.addMarker(
                MarkerOptions().position(latLngArrayList.get(i))
                    .title("Marker in " + locationNameArraylist.get(i))
                //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.mano_dcha_marker))
            )

        }
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(Madrid, 15f),
            4000,
            null
        )

        map.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            // which is clicked and displaying it in a toast message.
            val markerName = marker.title
            Toast.makeText(getActivity(), "Clicked location is $markerName", Toast.LENGTH_SHORT).show();
            false
        })
    }

    private fun requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(getActivity(),"GO TO SETTINGS AND ACCEPT THE LOCATION PERMISSION", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapsActivity.REQUEST_CODE_LOCATION
            )
        }
    }
    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{

        }
    }
    private fun isLocationPermissionGranted()= ContextCompat.checkSelfPermission(
        requireActivity(),
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(getActivity(),"Estas en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }


}