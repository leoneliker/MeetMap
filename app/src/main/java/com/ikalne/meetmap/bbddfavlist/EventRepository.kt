package com.ikalne.meetmap.bbddfavlist

import android.app.Application
import androidx.lifecycle.LiveData

class EventRepository(application: Application) {

    private val mEventDao: EventDaoDao
    val allContacts: LiveData<List<Event>>

    init {
        val db = EventRoomDatabase.getDatabase(application)
        mEventDao = db.EventDaoDao()
        allContacts = mEventDao.getAlphabetizedContact()
    }

    // Room ejecuta todas las consultas en un hilo separado.
    // LiveData observado notificará al observador cuando los datos cambien.
    fun getAllContact(): LiveData<List<Event>> {
        return allContacts
    }

    // Debe llamar a esto en un hilo que no sea de la IU o su aplicación lanzará una excepción. Room se asegura
    // de que no está haciendo ninguna operación de larga duración en el hilo principal, bloqueando la IU.
    fun insert(event: Event) {
        EventRoomDatabase.databaseWriteExecutor.execute {
            mEventDao.insert(event)
        }
    }

    fun delete(contact: Contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute {
            mContactDao.delete(contact.mName.toString(), contact.mPhone.toString())
        }
    }
}