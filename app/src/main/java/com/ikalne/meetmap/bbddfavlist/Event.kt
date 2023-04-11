package com.ikalne.meetmap.bbddfavlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ev_id")
    var event_id: Int,
    @ColumnInfo(name = "ev_name")
    var event_name: String,
    @ColumnInfo(name = "ev_date")
    var event_date: String,
    @ColumnInfo(name = "ev_time")
    var event_time: String,
    @ColumnInfo(name = "ev_place")
    var event_place: String
) : Serializable