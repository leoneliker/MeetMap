package com.ikalne.meetmap.api.models


class LocatorView(
    var id: String = "",
    var title: String = "",
    var location: Location = Location()
){
    fun fromGraph(graph: graph): LocatorView {
        var newLocation: Location = Location()
        graph.location?.let { newLocation = it }
        return LocatorView(
            id = graph.id.toString(), title = graph.title.toString(), location = newLocation
        )
    }

    override fun toString(): String {
        return "LocatorView(id='$id', title='$title', location=$location)"
    }
}



