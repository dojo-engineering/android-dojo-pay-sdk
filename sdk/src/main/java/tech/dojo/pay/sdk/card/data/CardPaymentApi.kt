package tech.dojo.pay.sdk.card.data

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
import tech.dojo.pay.sdk.card.data.entities.DeviceDataRequest
import tech.dojo.pay.sdk.card.data.entities.DeviceData

internal interface CardPaymentApi {

    @POST("device-data/{token}")
    suspend fun collectDeviceData(
        @Path("token") token: String,
        @Body payload: DeviceDataRequest
    ) : DeviceData

    @POST
    @FormUrlEncoded
    suspend fun handleDataCollection(
        @Url url: String,
        @Field("JWT") jwt: String
    )
}