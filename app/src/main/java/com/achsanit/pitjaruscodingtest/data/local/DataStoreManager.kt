package com.achsanit.pitjaruscodingtest.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    suspend fun setLoginStatus(isLogin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = isLogin
        }
    }

    suspend fun setUsername(username: String) {
        context.dataStore.edit {
            it[USERNAME_KEY] = username
        }
    }

    fun getUsername(): Flow<String> {
        return context.dataStore.data.map {
            it[USERNAME_KEY] ?: ""
        }
    }

    companion object {
        const val DATA_STORE_NAME = "my_data_store"

        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val LOGIN_KEY = booleanPreferencesKey("login_key")
    }
}