package com.ikalne.meetmap.bbddfavlist

import android.app.Application
import androidx.lifecycle.LiveData


class EventRepository(application: Application) {

    private val mEventDao: EventDao
    private val allEvents: LiveData<List<Event>>

    init {
        val db = EventRoomDatabase.getDatabase(application)
        mEventDao = db.eventDao()
        allEvents = mEventDao.getAlphabetizedEvent()
    }

    fun getAllEvents(): LiveData<List<Event>> {
        return allEvents
    }

    fun insert(event: Event) {
        EventRoomDatabase.databaseWriteExecutor.execute {
            mEventDao.insert(event)
        }
    }

    fun delete(event: Event) {
        EventRoomDatabase.databaseWriteExecutor.execute {
            mEventDao.delete(event.event_id)
        }
    }
}