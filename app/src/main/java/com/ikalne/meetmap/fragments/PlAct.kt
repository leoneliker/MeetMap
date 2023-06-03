package com.ikalne.meetmap.fragments

import java.io.Serializable

data class plAct(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val horario: String,
    val lugar: String,
    val cat: String
) : Serializable