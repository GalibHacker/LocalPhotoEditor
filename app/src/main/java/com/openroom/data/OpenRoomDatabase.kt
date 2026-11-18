package com.openroom.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class OpenRoomDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
