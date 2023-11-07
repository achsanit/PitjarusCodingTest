package com.achsanit.pitjaruscodingtest.ui.fragment.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.achsanit.pitjaruscodingtest.data.local.DataStoreManager

class SplashViewModel(
    private val pref: DataStoreManager
) : ViewModel() {

    fun isLogin() : LiveData<Boolean> {
        return pref.isLoggedIn().asLiveData()
    }
}