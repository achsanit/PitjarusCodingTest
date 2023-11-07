package com.achsanit.pitjaruscodingtest.ui.fragment.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achsanit.pitjaruscodingtest.data.local.DataStoreManager
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val pref: DataStoreManager
): ViewModel() {

    fun setStatusLogin(isLoggedIn: Boolean) {
        viewModelScope.launch {
            pref.setLoginStatus(isLoggedIn)
        }
    }

}