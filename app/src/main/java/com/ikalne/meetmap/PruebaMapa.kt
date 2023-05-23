package com.ikalne.meetmap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.view.ClusterRenderer
import com.ikalne.meetmap.databinding.ActivityPruebaMapaBinding
import com.ikalne.meetmap.model.MyClusterRenderer
import com.ikalne.meetmap.model.MyItem

class PruebaMapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<MyItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_mapa)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val madridLatLng = LatLng(40.4168, -3.7038)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(madridLatLng, 12f))
        setupClusterManager()
        val clusterRenderer = MyClusterRenderer(this, map, clusterManager)
        clusterManager.renderer = clusterRenderer
        addClusterItems()
    }

    private fun setupClusterManager() {
        clusterManager = ClusterManager(this, map)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
    }

    private fun addClusterItems() {
        val points = listOf(
            LatLng(40.4169, -3.7042),
            LatLng(40.4167, -3.7036),
            LatLng(40.4165, -3.7040),
            LatLng(40.4163, -3.7035),
            LatLng(40.4161, -3.7041),
            LatLng(40.4159, -3.7038),
            LatLng(40.4157, -3.7042),
            LatLng(40.4155, -3.7037),
            LatLng(40.4153, -3.7043),
            LatLng(40.4151, -3.7039),
            LatLng(40.4149, -3.7045),
            LatLng(40.4147, -3.7040),
            LatLng(40.4145, -3.7044),
            LatLng(40.4143, -3.7039),
            LatLng(40.4141, -3.7043),
            LatLng(40.4139, -3.7038),
            LatLng(40.4137, -3.7042),
            LatLng(40.4135, -3.7037),
            LatLng(40.4133, -3.7043),
            LatLng(40.4131, -3.7039)
        )

        for (point in points) {
            val item = MyItem(point, "Nombre Inventado", "Fecha Inventada")
            clusterManager.addItem(item)
        }
        clusterManager.cluster()
    }
}