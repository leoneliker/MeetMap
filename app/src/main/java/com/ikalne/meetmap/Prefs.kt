package com.ikalne.meetmap

import android.content.Context

class Prefs(val context:Context)
{
    val SHARED_DATA="Mydtb"
    val SHARED_USER_NAME="username"
    val SHARED_PASSWORD="password"
    val SHARED_REPASSWORD="repassword"
    val storage = context.getSharedPreferences(SHARED_DATA, 0)

    fun saveName(name:String)
    {
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }

    fun savePass(password:String)
    {
        storage.edit().putString(SHARED_PASSWORD, password).apply()
    }

    fun saveRePass(repassword:String)
    {
        storage.edit().putString(SHARED_REPASSWORD, repassword).apply()
    }

    fun getName():String
    {
        return storage.getString(SHARED_USER_NAME, "")!!
    }

    fun getPassword():String
    {
        return storage.getString(SHARED_PASSWORD, "")!!
    }

    fun getRePassword():String
    {
        return storage.getString(SHARED_REPASSWORD, "")!!
    }
}