package com.ikalne.meetmap.bbddfavlist

import android.app.Application
import androidx.lifecycle.LiveData

class EventRepository(application: Application) {

    private val mEventDao: EventDao
    val allEvents: LiveData<List<Event>>

    init {
        val db = EventRoomDatabase.getDatabase(application)
        mEventDao = db.EventDao()
        allEvents = mEventDao.getAlphabetizedContact()
    }

    // Room ejecuta todas las consultas en un hilo separado.
    // LiveData observado notificará al observador cuando los datos cambien.
    fun getAllContact(): LiveData<List<Event>> {
        return allEvents
    }

    // Debe llamar a esto en un hilo que no sea de la IU o su aplicación lanzará una excepción. Room se asegura
    // de que no está haciendo ninguna operación de larga duración en el hilo principal, bloqueando la IU.
    fun insert(event: Event) {
        EventRoomDatabase.databaseWriteExecutor.execute {
            mEventDao.insert(event)
        }
    }

    fun delete(event: Event) {
        EventRoomDatabase.databaseWriteExecutor.execute {
            mEventDao.delete(event.event_id.toString())
        }
    }
}