package com.ikalne.meetmap.bbddfavlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: EventRepository
    private val mAllEvents: LiveData<List<Event>>

    init {
        mRepository = EventRepository(application)
        mAllEvents = mRepository.getAllEvents()
    }

    fun getAllEvents(): LiveData<List<Event>> {
        return mAllEvents
    }

    fun insert(Event: Event) {
        mRepository.insert(Event)
    }

    fun delete(Event: Event) {
        mRepository.delete(Event)
    }
}