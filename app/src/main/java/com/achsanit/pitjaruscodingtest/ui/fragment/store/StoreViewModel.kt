package com.achsanit.pitjaruscodingtest.ui.fragment.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.achsanit.pitjaruscodingtest.domain.UseCase
import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import com.google.android.gms.maps.model.LatLng

class StoreViewModel(private val useCase: UseCase): ViewModel() {

    fun getListStore(): LiveData<List<StoreEntity>> {
        return useCase.getListStore().asLiveData()
    }
}