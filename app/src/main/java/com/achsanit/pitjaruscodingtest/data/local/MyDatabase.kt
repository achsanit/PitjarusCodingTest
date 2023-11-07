package com.achsanit.pitjaruscodingtest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity

@Database(
    entities = [
        StoreEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MyDatabase: RoomDatabase() {
    abstract fun dbDao(): DbDao
}