package com.ikalne.meetmap.models

import android.content.Context

class Prefs(val context:Context)
{
    val SHARED_DATA="Mydtb"
    val SHARED_USER_EMAIL="useremail"
    val SHARED_PASSWORD="password"
    val SHARED_REPASSWORD="repassword"
    val storage = context.getSharedPreferences(SHARED_DATA, 0)

    fun saveEmail(email:String)
    {
        storage.edit().putString(SHARED_USER_EMAIL, email).apply()
    }

    fun savePass(password:String)
    {
        storage.edit().putString(SHARED_PASSWORD, password).apply()
    }

    fun saveRePass(repassword:String)
    {
        storage.edit().putString(SHARED_REPASSWORD, repassword).apply()
    }

    fun getEmail():String
    {
        return storage.getString(SHARED_USER_EMAIL, "")!!
    }

    fun getPassword():String
    {
        return storage.getString(SHARED_PASSWORD, "")!!
    }

    fun getRePassword():String
    {
        return storage.getString(SHARED_REPASSWORD, "")!!
    }

    fun wipe()
    {
        storage.edit().clear().apply()
    }
}