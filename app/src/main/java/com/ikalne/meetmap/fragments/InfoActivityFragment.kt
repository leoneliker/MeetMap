package com.ikalne.meetmap.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.Marker
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.bbddfavlist.Event
import com.ikalne.meetmap.databinding.FragmentInfoActivityBinding

class InfoActivityFragment : Fragment() {

    private lateinit var binding: FragmentInfoActivityBinding
    private lateinit var marker: Marker
    private lateinit var locatorsList: List<LocatorView>
    private lateinit var imgbtn: ImageButton
    private var isGreen = false

    fun setMarker(marker: Marker, locatorsList: List<LocatorView>) {
        this.marker = marker
        this.locatorsList = locatorsList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoActivityBinding.inflate(inflater, container, false)

        val idInfo = MapFragment.madridMap[marker.title]

        imgbtn = binding.btnedit
        imgbtn.setOnClickListener {
            if (isGreen) {
                imgbtn.setBackgroundResource(R.drawable.love2);
                isGreen = false
            } else {
                imgbtn.setBackgroundResource(R.drawable.love);
                isGreen = true
            }
        }

        locatorsList.find { it.id == idInfo }?.let { fillFields(it) }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun fillFields(locatorView: LocatorView) {
        binding.apply {
            titulo.text = locatorView.title

            val desc = if (locatorView.description.isEmpty()) {
                resources.getString(R.string.noHayInfo)
            } else {
                locatorView.description
            }
            description.text = desc

            val date = if (locatorView.dstart.split(" ")[0] == locatorView.dfinish.split(" ")[0]) {
                locatorView.dstart.split(" ")[0]
            } else if (locatorView.dstart.isEmpty() && locatorView.dfinish.isEmpty()) {
                resources.getString(R.string.dateInfo)
            } else {
                locatorView.dstart.split(" ")[0] + " - " + locatorView.dfinish.split(" ")[0]
            }
            fecha.text = date.toString()

            val hora = locatorView.time.ifEmpty {
                resources.getString(R.string.hourInfo)
            }
            horario.text = hora

            val lug = locatorView.event_location.ifEmpty {
                resources.getString(R.string.placeInfo)
            }
            lugar.text = lug
            link.text = resources.getString(R.string.link)

            link.setOnClickListener {
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            info.setOnClickListener {
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            if (isGreen) {
                val event = Event(
                    id ?: 0,
                    titulo.text.toString(),
                    fecha.text.toString(),
                    horario.text.toString(),
                    lugar.text.toString()
                )
                //binding.link.setPaintFlags(binding.link.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }
        }
    }
}




