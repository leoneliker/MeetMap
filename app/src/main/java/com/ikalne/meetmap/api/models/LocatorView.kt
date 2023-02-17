package com.ikalne.meetmap.api.models


class LocatorView(
    var id: String = "",
    var title: String = "",
    var location: Location = Location(),
    var description: String = "",
    var category: String =  "",
    var event_location: String = "",
    var time: String = "",
    var price: String = "",
    var dstart: String = "",
    var dfinish: String = "",
    var link: String = "",
){
    fun fromGraph(graph: graph): LocatorView {
        var newLocation: Location = Location()
        graph.location?.let { newLocation = it }
        return LocatorView(
            id = graph.id.toString(),
            title = graph.title.toString(),
            location = newLocation,
            description = graph.description.toString(),
            category = graph.type.toString(),
            event_location = graph.eventLocation.toString(),
            time= graph.time.toString(),
            price= graph.price.toString(),
            dstart = graph.dtstart.toString(),
            dfinish = graph.dtend.toString(),
            link = graph.link.toString()
        )
    }

    override fun toString(): String {
        return "LocatorView(id='$id', title='$title', location=$location, description='$description', category='$category', event_location='$event_location', time='$time', price='$price', dstart='$dstart', dfinish='$dfinish', link='$link')"
    }


}



