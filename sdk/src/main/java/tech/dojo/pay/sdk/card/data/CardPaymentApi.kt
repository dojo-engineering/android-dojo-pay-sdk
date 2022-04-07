package tech.dojo.pay.sdk.card.data

import retrofit2.http.POST
import retrofit2.http.Path

interface CardPaymentApi {

    @POST("device-data/{token}")
    suspend fun collectDeviceData(@Path("token") token: String) {

    }
}