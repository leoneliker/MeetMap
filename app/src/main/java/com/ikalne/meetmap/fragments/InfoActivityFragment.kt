package com.ikalne.meetmap.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import com.ikalne.meetmap.R
import com.ikalne.meetmap.api.models.LocatorView
import com.ikalne.meetmap.databinding.FragmentInfoActivityBinding

class InfoActivityFragment :Fragment() {

    lateinit var imgLayout: ImageView
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

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun fillFields(locatorView: LocatorView) {
        binding.apply {

            imgLayout = binding.ivevent
            val res = when(locatorView.category.split("/")[6]) {
                "Musica" -> resources.getDrawable(R.drawable.friends, null)
                "DanzaBaile" -> resources.getDrawable(R.drawable.friends, null)
                "CursosTalleres" -> resources.getDrawable(R.drawable.friends, null)
                "TeatroPerformance" -> resources.getDrawable(R.drawable.friends, null)
                "ActividadesCalleArteUrbano" -> resources.getDrawable(R.drawable.friends, null)
                "CuentacuentosTiteresMarionetas" -> resources.getDrawable(R.drawable.friends, null)
                "ProgramacionDestacadaAgendaCultura" -> resources.getDrawable(R.drawable.friends, null)
                "ComemoracionesHomenajes" -> resources.getDrawable(R.drawable.friends, null)
                "ConferenciasColoquios" -> resources.getDrawable(R.drawable.friends, null)
                "1ciudad21distritos" -> resources.getDrawable(R.drawable.friends, null)
                "ExcursionesItinerariosVisitas" -> resources.getDrawable(R.drawable.friends, null)
                "ItinerariosOtrasActividadesAmbientales" -> resources.getDrawable(R.drawable.friends, null)
                "ClubesLectura" -> resources.getDrawable(R.drawable.friends, null)
                "RecitalesPresentacionesActosLiterarios" -> resources.getDrawable(R.drawable.friends, null)
                "Exposiciones" -> resources.getDrawable(R.drawable.friends, null)
                "Campamentos" -> resources.getDrawable(R.drawable.friends, null)
                "CineActividadesAudiovisuales" -> resources.getDrawable(R.drawable.friends, null)
                "CircoMagia" -> resources.getDrawable(R.drawable.friends, null)
                "FiestasSemanaSanta" -> resources.getDrawable(R.drawable.friends, null)

                else -> resources.getDrawable(R.drawable.workin, null)
            }


            activity?.let { Glide.with(it)
                                .load(res)
                                .into(imgLayout)}


            titulo.text = locatorView.title

            val desc = locatorView.description.ifEmpty {
                resources.getString(R.string.noHayInfo)
            }
            description.text = desc

            val date = if (locatorView.dstart.split(" ")[0]==locatorView.dfinish.split(" ")[0]){
                locatorView.dstart.split(" ")[0]
            }else if(locatorView.dstart.isEmpty() && locatorView.dfinish.isEmpty()){
                resources.getString(R.string.dateInfo)
            }
            else{
                locatorView.dstart.split(" ")[0]+" - "+locatorView.dfinish.split(" ")[0]
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
           //binding.link.setPaintFlags(binding.link.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            link.setOnClickListener{
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            info.setOnClickListener{
                val url = locatorView.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }


}




