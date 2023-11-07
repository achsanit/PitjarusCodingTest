package com.achsanit.pitjaruscodingtest.domain

import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import com.achsanit.pitjaruscodingtest.util.Resource
import kotlinx.coroutines.flow.Flow

class Interactor(private val repository: IRepository): UseCase {

    override suspend fun login(username: String, password: String): Resource<Boolean> {
        return repository.login(username, password)
    }

    override fun getListStore(): Flow<List<StoreEntity>> {
        return repository.getListStore()
    }

    override suspend fun updateStatusVisit(status: Boolean, idStore: Int) {
        return repository.updateStatusVisit(status, idStore)
    }

}