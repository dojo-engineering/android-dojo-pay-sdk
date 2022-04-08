package tech.dojo.pay.sdk.card.data

import retrofit2.http.POST

interface CardPaymentApi {

    @POST("device-data")
    suspend fun collectDeviceData()
}