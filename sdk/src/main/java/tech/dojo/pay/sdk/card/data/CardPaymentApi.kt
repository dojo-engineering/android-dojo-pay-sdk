package tech.dojo.pay.sdk.card.data

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import tech.dojo.pay.sdk.card.data.entities.DeviceDataRequest
import tech.dojo.pay.sdk.card.data.entities.DeviceData

internal interface CardPaymentApi {

    @POST("device-data/{token}")
    suspend fun collectDeviceData(
        @Path("token") token: String,
        @Body payload: DeviceDataRequest
    ) : DeviceData
}