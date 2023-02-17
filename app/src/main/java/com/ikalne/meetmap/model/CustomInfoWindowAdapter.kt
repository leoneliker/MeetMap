package com.ikalne.meetmap.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.api.services.MadridApiRequestManager
import com.ikalne.meetmap.fragments.MapFragment
import com.ikalne.meetmap.viewmodels.MadridViewModel


class CustomInfoWindowAdapter(val inflater: LayoutInflater, val locatorsList: List<LocatorView>) : InfoWindowAdapter {

    override fun getInfoContents(m: Marker): View? {

        return null
    }

    init {
        Log.i("assssjhgvhgvgh", "$locatorsList")
    }

    override fun getInfoWindow(m: Marker): View? {


        val idInfo = MapFragment.madridMap[m.title]
        locatorsList.forEach {
            if (it.id == idInfo) {
                val v: View = inflater.inflate(R.layout.infowindow_layout, null)
                val info = m.title.split("&").toTypedArray()
                val url = m.snippet
                (v.findViewById(R.id.info_window_nombre) as TextView).text = it.title
                (v.findViewById(R.id.info_window_placas) as TextView).text = "${it.dstart.split(" ")[0]}-${it.dfinish.split(" ")[0]}"
                (v.findViewById(R.id.info_window_estado) as TextView).text = it.time
                return v
            }
        }
        return null
    }
}