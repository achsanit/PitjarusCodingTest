package com.achsanit.pitjaruscodingtest.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.achsanit.pitjaruscodingtest.di.KoinInitializer

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        KoinInitializer.init(this)
    }
}