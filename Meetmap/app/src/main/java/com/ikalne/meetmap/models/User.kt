package com.ikalne.meetmap.models

import com.google.firebase.auth.FirebaseUser

data class User (
    var id: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var pass: String = "",
    var desc: String = "",
    var numTelf: Number = 0,
    var photo: String = ""
)