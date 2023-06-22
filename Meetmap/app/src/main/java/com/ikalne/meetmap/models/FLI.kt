package com.ikalne.meetmap.models

class FLI {
    var id: String=""
    var titulo: String=""
    var fecha: String=""
    var horario: String=""
    var lugar: String=""
    var isFav: Boolean=false

    constructor()

    constructor(
        id:String,
        titulo: String,
        fecha: String,
        horario: String,
        lugar: String,
        isFav: Boolean
    ){
        this.id = id
        this.titulo = titulo
        this.fecha = fecha
        this.horario = horario
        this.lugar = lugar
        this.isFav= isFav
    }
}