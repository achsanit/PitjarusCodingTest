package com.achsanit.pitjaruscodingtest.util.mapper

import com.achsanit.pitjaruscodingtest.data.network.response.LoginResponse
import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import com.achsanit.pitjaruscodingtest.util.Resource
import com.achsanit.pitjaruscodingtest.util.statics.CodeError
import com.google.android.gms.maps.model.LatLng
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun LoginResponse.loginStatus() : Boolean {
    val status = this.status ?: LoginStatus.Failure.name.lowercase()

    return status == LoginStatus.Success.name.lowercase()
}

fun LoginResponse.mapToEntity() : List<StoreEntity>? {
    return this.stores?.map { storeItem ->
        StoreEntity(
            storeId = storeItem?.storeId?.toInt() ?: 0,
            storeName = storeItem?.storeName ?: "",
            latitude = storeItem?.latitude ?: "",
            longitude = storeItem?.longitude ?: ""
        )
    }
}

fun List<StoreEntity>.mapToListLatLang(): List<LatLng> {
    return this.map {
        LatLng(
            it.latitude.toDouble(),
            it.longitude.toDouble()
        )
    }
}

enum class LoginStatus {
    Success, Failure
}