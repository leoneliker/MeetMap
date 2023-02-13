package com.ikalne.meetmap

import android.content.Context
import androidx.fragment.app.FragmentActivity

object PreferencesManager{

    fun getDefaultSharedPreferences(context: Context) : Prefs{
        return Prefs(context)
    }
}