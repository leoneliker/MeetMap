package com.ikalne.meetmap.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyItem(
    private val position: LatLng,
    private val nombre: String,
    private val categoria: String
) : ClusterItem {

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return nombre
    }

    override fun getSnippet(): String? {
        return fecha
    }

    fun getNombre(): String {
        return nombre
    }

    fun getCategoria(): String {
        return categoria
    }
}
