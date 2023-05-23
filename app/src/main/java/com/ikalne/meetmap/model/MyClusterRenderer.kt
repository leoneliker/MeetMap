package com.ikalne.meetmap.model

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.ikalne.meetmap.R

class MyClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MyItem>
) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {

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
        marker.tag = clusterItem.getNombre() // Optional: You can set a tag to the marker for future reference
    }
}
