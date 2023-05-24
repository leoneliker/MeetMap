package com.ikalne.meetmap.model

import android.app.AlertDialog
import android.content.Context
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.ikalne.meetmap.R
import com.ikalne.meetmap.fragments.InfoActivityFragment
import com.ikalne.meetmap.fragments.MapFragment

class MyClusterRenderer(
    private val context: Context,
    private val map: GoogleMap,
    clusterManager: ClusterManager<MyItem>,
    private val fragmentManager: FragmentManager

) : DefaultClusterRenderer<MyItem>(context, map, clusterManager), ClusterManager.OnClusterClickListener<MyItem> {


    private val clusterIcon: BitmapDescriptor =
        BitmapDescriptorFactory.fromResource(R.drawable.mano_rosa)

    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions.title(item.getNombre())
        markerOptions.icon(clusterIcon)
        markerOptions.snippet(item.getFecha())
    }

    override fun getColor(clusterSize: Int): Int {
        return if (clusterSize >= 20) {
            context.resources.getColor(R.color.secondary_dark)
        } else {
            context.resources.getColor(R.color.secondary)
        }
    }

    override fun onClusterItemRendered(clusterItem: MyItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        marker.setIcon(clusterIcon)
        marker.tag = clusterItem.getNombre()
    }

    override fun onClusterClick(cluster: Cluster<MyItem>): Boolean {
        val zoomLevel = map.cameraPosition.zoom
        if (zoomLevel >18) {
            val items = ArrayList<String>()
            for (item in cluster.items) {
                val title = item.getNombre()
                items.add(title)
            }
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle("Elementos agrupados")
            dialogBuilder.setItems(items.toTypedArray()) { dialog, which ->
                val selectedItem = cluster.items.elementAtOrNull(which)
                if (selectedItem != null) {
                    val marker = MapFragment.markers[selectedItem.getNombre()]
                    if (marker != null) {
                        val infoFragment = InfoActivityFragment()
                        infoFragment.setMarker(marker, MapFragment.locatorListFav)
                        val fragmentManager = this.fragmentManager
                        fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.frame, infoFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        } else {
            Toast.makeText(context, "Debes acercarte más para acceder a ese conjunto de actividades", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}
