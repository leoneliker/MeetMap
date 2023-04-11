package com.ikalne.meetmap.bbddfavlist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.reflect.KParameter
import androidx.lifecycle.LiveData

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventRoomDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventRoomDatabase? = null

        fun getDatabase(context: Context): EventRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventRoomDatabase::class.java,
                    "event_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(4)
    }
}