package com.ikalne.meetmap.models

import android.content.Context

object PreferencesManager{

    fun getDefaultSharedPreferences(context: Context) : Prefs {
        return Prefs(context)
    }
}