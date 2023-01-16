package com.ikalne.meetmap

import android.app.Application

class MeetMapApplication : Application ()
{
    companion object
    {
        lateinit var prefs:Prefs
    }
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}