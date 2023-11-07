package com.achsanit.pitjaruscodingtest.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.achsanit.pitjaruscodingtest.data.local.DataStoreManager
import com.achsanit.pitjaruscodingtest.domain.UseCase
import com.achsanit.pitjaruscodingtest.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(
    private val useCase: UseCase,
    private val pref: DataStoreManager
) : ViewModel() {

    private val _loginStatus: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val loginStatus: LiveData<Resource<Boolean>> = _loginStatus

    fun login(username: String, password: String) {
        _loginStatus.postValue(Resource.Loading())
        viewModelScope.launch {
            _loginStatus.postValue(useCase.login(username, password))
        }
    }

    fun setLoginStatus(isLogin: Boolean) {
        viewModelScope.launch {
            pref.setLoginStatus(isLogin)
        }
    }

    fun saveUsername(username: String) {
        viewModelScope.launch {
            pref.setUsername(username)
        }
    }

    fun getUsername(): LiveData<String> {
        return pref.getUsername().asLiveData()
    }
}