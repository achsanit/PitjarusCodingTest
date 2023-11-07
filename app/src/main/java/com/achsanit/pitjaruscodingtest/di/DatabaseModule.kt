package com.achsanit.pitjaruscodingtest.di

import androidx.room.Room
import com.achsanit.pitjaruscodingtest.data.local.DataStoreManager
import com.achsanit.pitjaruscodingtest.data.local.MyDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    // provide room db
    single {
        Room.databaseBuilder(
            androidContext(),
            MyDatabase::class.java,
            "my.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    // provide dao
    factory { get<MyDatabase>().dbDao() }

    // preferences
    single { DataStoreManager(androidContext()) }
}