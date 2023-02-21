package com.ikalne.meetmap.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.Marker
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.databinding.FragmentInfoActivityBinding

class InfoActivityFragment :Fragment() {

    private lateinit var binding : FragmentInfoActivityBinding
    private lateinit var marker: Marker
    private lateinit var locatorsList: List<LocatorView>

    fun setMarker(marker: Marker, locatorsList: List<LocatorView>) {
        this.marker = marker
        this.locatorsList = locatorsList
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoActivityBinding.inflate(inflater, container, false)

        val idInfo = MapFragment.madridMap[marker.title]

        locatorsList.find { it.id==idInfo }?.let { fillFields(it) }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun fillFields(locatorView: LocatorView) {
        binding.apply {

            titulo.text = locatorView.title

            val desc = if (locatorView.description.isEmpty()){
                "La empresa de la actividad no ofrece mayor información, dirigete al centro o a la web para encontrarla"
            }else{
                locatorView.description
            }
            description.text = desc

            val date = if (locatorView.dstart.split(" ")[0]==locatorView.dfinish.split(" ")[0]){
                locatorView.dstart.split(" ")[0]
            }else if(locatorView.dstart.isEmpty() && locatorView.dfinish.isEmpty()){
                "No hay fecha establecida"
            }
            else{
                locatorView.dstart.split(" ")[0]+" - "+locatorView.dfinish.split(" ")[0]
            }
            fecha.text = date.toString()

            val hora = locatorView.time.ifEmpty {
                "No hay hora establecida"
            }
            horario.text = hora

            val lug = locatorView.event_location.ifEmpty {
                "No hay lugar establecido"
            }
            lugar.text = lug
            link.text = "Haz click aquí para redirigirte a la página web"
            binding.link.setPaintFlags(binding.link.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            link.setOnClickListener{
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }


}




