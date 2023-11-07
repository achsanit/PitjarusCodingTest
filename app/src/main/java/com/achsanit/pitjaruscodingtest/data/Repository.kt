package com.achsanit.pitjaruscodingtest.data

import com.achsanit.pitjaruscodingtest.data.local.DbDao
import com.achsanit.pitjaruscodingtest.data.network.ApiService
import com.achsanit.pitjaruscodingtest.util.Resource
import com.achsanit.pitjaruscodingtest.domain.IRepository
import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import com.achsanit.pitjaruscodingtest.util.mapper.loginStatus
import com.achsanit.pitjaruscodingtest.util.mapper.resourceMapper
import com.achsanit.pitjaruscodingtest.util.mapper.mapToEntity
import com.achsanit.pitjaruscodingtest.util.statics.listStaticStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class Repository(
    private val apiService: ApiService,
    private val dbDao: DbDao
): IRepository {
    override suspend fun login(username: String, password: String): Resource<Boolean> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username",username)
            .addFormDataPart("password",password)
            .build()

        return resourceMapper(
            code = {
                apiService.login(requestBody)
            },
            convertData = { loginResponse ->
                loginResponse.loginStatus()
            },
            saveResult = { loginResponse ->
                loginResponse.mapToEntity()?.let {
                    dbDao.deleteAndInsertStore(it)
                    dbDao.insertListStore(listStaticStore)
                }
            }
        )
    }

    override fun getListStore(): Flow<List<StoreEntity>> {
        return dbDao.getListStore()
    }

    override suspend fun updateStatusVisit(status: Boolean, idStore: Int) {
        return dbDao.updateStatusVisit(status, idStore)
    }
}