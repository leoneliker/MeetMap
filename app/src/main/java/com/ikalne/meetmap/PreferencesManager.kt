package com.ikalne.meetmap

import android.content.Context

object PreferencesManager{

    fun getDefaultSharedPreferences(context: Context) : Prefs{
        return Prefs(context)
    }
}