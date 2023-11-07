package com.achsanit.pitjaruscodingtest.data.network

import com.achsanit.pitjaruscodingtest.data.network.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login/loginTest")
    suspend fun login(
        @Body body: RequestBody
    ): LoginResponse
}