package com.ikalne.meetmap.bbddfavlist

import android.content.Context
import androidx.room.Database
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.reflect.KParameter

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventRoomDatabase : RoomDatabase() {

    abstract fun EventDao(): EventDaoDao

    // marking the instance as volatile to ensure atomic access to the variable
    @Volatile
    private var INSTANCE: EventRoomDatabase? = null
    private val NUMBER_OF_THREADS = 4
    val databaseWriteExecutor: ExecutorService =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    companion object {
        @JvmStatic
        fun getDatabase(context: Context): KParameter.Kind {
            return KParameter.Kind.INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventRoomDatabase::class.java,
                    "event_table"
                )
                    .addCallback(sRoomDatabaseCallback)
                    .build()
                KParameter.Kind.INSTANCE = instance
                instance
            }
        }
    }
}