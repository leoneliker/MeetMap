package com.ikalne.meetmap.models

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.ikalne.meetmap.selectionIcon

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
        markerOptions.snippet(item.getCategory())
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
        if (zoomLevel > 15) {
            val items = ArrayList<String>()
            val categories = ArrayList<String>()
            for (item in cluster.items) {
                val title = item.getNombre().split(" ").drop(1).joinToString(" ")
                items.add(title)
                categories.add(item.getCategory())
            }

            val dialogBuilder = AlertDialog.Builder(context)
            val title = context.getString(R.string.pick_a_plan)
            val spannableTitle = SpannableString(title)
            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(context, R.color.secondary_dark))
            spannableTitle.setSpan(colorSpan, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            dialogBuilder.setTitle(spannableTitle)


            val adapter = object : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent) as TextView
                    val icon = ContextCompat.getDrawable(context,selectionIcon(categories[position]))
                    icon?.setBounds(0, 0, 75, 75)
                    view.setCompoundDrawables(icon, null, null, null)
                    view.compoundDrawablePadding = 16
                    view.gravity = Gravity.CENTER_VERTICAL
                    view.setPadding(10, 0, 0, 0)
                    view.setTextAppearance(context, android.R.style.TextAppearance_Medium)
                    return view
                }
            }

            dialogBuilder.setAdapter(adapter) { dialog, which ->
                val selectedItem = cluster.items.elementAtOrNull(which-1)
                if (selectedItem != null) {
                    val marker = MapFragment.markers[selectedItem.getNombre()]
                    Log.i("pruiea",selectedItem.getNombre())
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
            val listView = dialog.listView
            listView.divider = ColorDrawable(ContextCompat.getColor(context, R.color.dark_gray))
            listView.dividerHeight = 1
            listView.addHeaderView(View(context), null, false)
            dialog.show()
        } else {
            Toast.makeText(context, context.getString(R.string.near_plans), Toast.LENGTH_SHORT).show()
        }
        return true
    }

}
