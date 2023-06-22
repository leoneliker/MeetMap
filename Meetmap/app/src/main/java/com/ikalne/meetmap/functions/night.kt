package com.ikalne.meetmap

import androidx.appcompat.app.AppCompatDelegate

fun forceLightMode() {
    try{
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }catch (e:java.lang.Exception){
        e.printStackTrace()
    }
}