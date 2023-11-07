package com.achsanit.pitjaruscodingtest.domain

import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import com.achsanit.pitjaruscodingtest.util.Resource
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun login(
        username: String,
        password: String
    ): Resource<Boolean>

    fun getListStore(): Flow<List<StoreEntity>>

    suspend fun updateStatusVisit(
        status: Boolean,
        idStore: Int
    )
}