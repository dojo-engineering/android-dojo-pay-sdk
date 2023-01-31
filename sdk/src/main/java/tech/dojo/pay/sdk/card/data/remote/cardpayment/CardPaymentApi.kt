package tech.dojo.pay.sdk.card.data.remote.cardpayment

import retrofit2.http.*
import tech.dojo.pay.sdk.card.data.entities.*
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails

internal interface CardPaymentApi {

    @POST("api/device-data/{token}")
    suspend fun collectDeviceData(
        @Path("token") token: String,
        @Body payload: PaymentDetails,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): DeviceData

    @POST("api/payments/{token}")
    suspend fun processPaymentForFullCard(
        @Path("token") token: String,
        @Body payload: PaymentDetails,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): PaymentResponse

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("payments/{token}/ThreeDSecureComplete")
    suspend fun processAuthorization(
        @Path("token") token: String,
        @Body payload: AuthorizationBody,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): PaymentResponse
    @POST("api/payments/recurring/{token}")
    suspend fun processPaymentForSaverCard(
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

    @POST("api/payments/{token}/google-pay")
    suspend fun processGPay(
        @Path("token") token: String,
        @Body payload: GPayDetails
    ): PaymentResponse
}
