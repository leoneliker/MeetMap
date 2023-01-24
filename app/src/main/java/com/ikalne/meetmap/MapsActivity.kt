package com.ikalne.meetmap

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    var Charla_Ted = LatLng(40.403940, -3.707746)
    var Poesia = LatLng(40.403351, -3.701352)
    var Hockey = LatLng(40.399824, -3.703590)
    var Teatro = LatLng(40.401490, -3.708010)

    var latLngArrayList: ArrayList<LatLng> = ArrayList()
    var locationNameArraylist: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        latLngArrayList!!.add(Charla_Ted);
        locationNameArraylist!!.add("Charla TED");
        latLngArrayList!!.add(Poesia);
        locationNameArraylist!!.add("Recital de poesia");
        latLngArrayList!!.add(Hockey);
        locationNameArraylist!!.add("Partido de hockey");
        latLngArrayList!!.add(Teatro);
        locationNameArraylist!!.add("Teatro: La maravilla de tu madre");
        requestLocationPermission()
        val mapFragment  = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        for (i in 0 until latLngArrayList.size) {
            map.addMarker(
                MarkerOptions().position(latLngArrayList.get(i))
                    .title("Marker in " + locationNameArraylist.get(i))
            )

        }
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableLocation()

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(Teatro, 10f),
            4000,
            null
        )

        map.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            // which is clicked and displaying it in a toast message.
            val markerName = marker.title
            Toast.makeText(this@MapsActivity, "Clicked location is $markerName", Toast.LENGTH_SHORT)
                .show()
            false
        })
    }


    //Metodo para crear un marker, sustituido por un array arriba.
    private fun createMarker() {
        val coordinates = LatLng(40.403108, -3.706084)
        val marker = MarkerOptions().position(coordinates).title("Prueba")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            4000,
            null
        )
    }

    private fun isLocationPermissionGranted()= ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{

        }
    }
    private fun requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this,"GO TO SETTINGS AND ACCEPT THE LOCATION PERMISSION", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled=true
            }else{
                Toast.makeText(this,"GO TO SETTINGS AND ACCEPT THE LOCATION PERMISSION", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized) return
        if(!isLocationPermissionGranted()){
            map.isMyLocationEnabled=false
            Toast.makeText(this,"GO TO SETTINGS AND ACCEPT THE LOCATION PERMISSION", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this,"Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this,"Estas en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }
}

