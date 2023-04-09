package com.ikalne.meetmap.models

import com.google.firebase.firestore.DocumentSnapshot

class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
){
    constructor(documentSnapshot: DocumentSnapshot) : this(
        id = documentSnapshot.id,
        name = documentSnapshot.getString("name") ?: "",
        users = documentSnapshot.get("users") as? List<String> ?: emptyList()
    )
}