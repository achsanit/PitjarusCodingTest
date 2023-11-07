package com.achsanit.pitjaruscodingtest.ui.fragment.visit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.achsanit.pitjaruscodingtest.domain.UseCase
import kotlinx.coroutines.launch

class VisitViewModel(
    private val useCase: UseCase
): ViewModel() {

    fun updateVisitStatus(status: Boolean, idStore: Int) {
        viewModelScope.launch {
            useCase.updateStatusVisit(status, idStore)
        }
    }
}