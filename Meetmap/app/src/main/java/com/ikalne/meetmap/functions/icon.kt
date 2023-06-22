package com.ikalne.meetmap

fun selectionIcon(
    category: String,
): Int {
    val options = listOf(
        R.drawable.ico_gen1,
        R.drawable.ico_gen2,
        R.drawable.ico_gen3,
        R.drawable.ico_gen4,
        R.drawable.ico_gen5
    )
    val iconResId = when (category.split("/").getOrNull(6) ?: options.random()) {
        "Musica" -> R.drawable.ico_musica
        "DanzaBaile" -> R.drawable.ico_danzabaile
        "CursosTalleres" -> R.drawable.ico_cursostalleres
        "TeatroPerformance" -> R.drawable.ico_teatro
        "ActividadesCalleArteUrbano" -> R.drawable.ico_arteurbano
        "CuentacuentosTiteresMarionetas" -> R.drawable.ico_cuentacuentos
        "ComemoracionesHomenajes" -> R.drawable.ico_homenaje
        "ConferenciasColoquios" -> R.drawable.ico_conferencias
        "1ciudad21distritos" -> R.drawable.ico_ciudaddistritos
        "ExcursionesItinerariosVisitas" -> R.drawable.ico_visitas
        "ItinerariosOtrasActividadesAmbientales" -> R.drawable.ico_ambientales
        "ClubesLectura" -> R.drawable.ico_lectura
        "RecitalesPresentacionesActosLiterarios" -> R.drawable.ico_recitales
        "Exposiciones" -> R.drawable.ico_exposiciones
        "Campamentos" -> R.drawable.ico_campamentos
        "CineActividadesAudiovisuales" -> R.drawable.ico_cine
        "CircoMagia" -> R.drawable.ico_circo
        "ProgramacionDestacadaAgendaCultura" -> R.drawable.ico_cultura
        "ActividadesDeportivas" -> R.drawable.ico_deportes
        "EnLinea" -> R.drawable.ico_enlinea
        else -> options.random()
    }
    return iconResId
}