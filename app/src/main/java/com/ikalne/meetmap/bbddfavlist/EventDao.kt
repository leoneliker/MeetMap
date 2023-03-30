package com.ikalne.meetmap.bbddfavlist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("SELECT * FROM event_table ORDER BY ev_date ASC")
    fun getAlphabetizedContact(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(Event: Event)

    @Query("DELETE FROM event_table")
    fun deleteAll()

    @Query("DELETE FROM event_table WHERE ev_id = :ev_id")
    fun delete(ev_id: String)
}