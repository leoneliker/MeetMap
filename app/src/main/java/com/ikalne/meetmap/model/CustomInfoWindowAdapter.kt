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

    override fun getInfoWindow(m: Marker): View? {


        val idInfo = MapFragment.madridMap[m.title]
        locatorsList.forEach {
            if (it.id == idInfo) {

                val v: View = inflater.inflate(R.layout.infowindow_layout, null)
                (v.findViewById(R.id.nombre) as TextView).text = it.title

                val date = if (it.dstart.split(" ")[0]==it.dfinish.split(" ")[0]){
                    it.dstart.split(" ")[0]
                }else if(it.dstart.isEmpty() && it.dfinish.isEmpty()){
                    "No hay fecha establecida"
                }
                else{
                    it.dstart.split(" ")[0]+" - "+it.dfinish.split(" ")[0]
                }
                (v.findViewById(R.id.fechas) as TextView).text = date

                val hora = it.time.ifEmpty {
                    "No hay hora establecida"
                }
                (v.findViewById(R.id.horas) as TextView).text = hora
                return v
            }
        }
        return null
    }
}