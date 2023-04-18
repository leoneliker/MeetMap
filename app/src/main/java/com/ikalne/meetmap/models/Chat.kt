package com.ikalne.meetmap.models

import com.google.firebase.firestore.DocumentSnapshot

class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList(),
    var delete: Boolean = false
){
    constructor(documentSnapshot: DocumentSnapshot) : this(
        id = documentSnapshot.id,
        name = documentSnapshot.getString("name") ?: "",
        users = documentSnapshot.get("users") as? List<String> ?: emptyList()
    )
}

/*Añadir un campo "borrado" a tu objeto de datos en Firebase. Este campo se utilizará para marcar si un objeto ha sido eliminado o no. Puedes establecer su valor en true cuando el usuario elimine un objeto y false cuando el usuario lo añada nuevamente.

En tu RecyclerView, tendrás que filtrar los datos que se muestran para mostrar solo los objetos que no han sido eliminados. Para hacer esto, puedes usar una consulta en Firebase que solo obtenga los objetos que no han sido eliminados (donde "borrado" es false).

Cuando un objeto se elimine del RecyclerView, tendrás que actualizar el valor "borrado" en Firebase a true. Esto se puede hacer mediante una transacción en Firebase, que asegurará que el valor se actualice de manera segura.

Cuando un objeto se añada de nuevo, tendrás que actualizar el valor "borrado" en Firebase a false para que se muestre en el RecyclerView nuevamente.

Al hacer esto, podrás mantener una lista completa de todos los objetos en Firebase, pero solo mostrar aquellos que no han sido eliminados en el RecyclerView. Cuando un objeto se elimine, seguirá estando en Firebase con el campo "borrado" establecido en true, lo que permitirá que se muestre nuevamente si se añade de nuevo en el futuro.*/