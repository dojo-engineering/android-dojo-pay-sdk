package tech.dojo.pay.sdk.card.data

import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Body
import retrofit2.http.Url
import retrofit2.http.Field
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.data.entities.PaymentResponse

internal interface CardPaymentApi {

    @POST("device-data/{token}")
    suspend fun collectDeviceData(
        @Path("token") token: String,
        @Body payload: PaymentDetails
    ): DeviceData

    @POST("payments/{token}")
    suspend fun processPayment(
        @Path("token") token: String,
        @Body payload: PaymentDetails
    ): PaymentResponse

    @POST
    @FormUrlEncoded
    suspend fun fetchSecurePage(
        @Url url: String,
        @Field("JWT") jwt: String,
        @Field("MD") md: String
    ): String

    @POST("payments/{token}/google-pay")
    suspend fun processGPay(
        @Path("token") token: String,
        @Body payload: GPayDetails
    ): PaymentResponse
}
