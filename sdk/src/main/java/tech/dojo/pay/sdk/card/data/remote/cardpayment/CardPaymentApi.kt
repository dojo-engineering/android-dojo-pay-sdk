package tech.dojo.pay.sdk.card.data.remote.cardpayment

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
import tech.dojo.pay.sdk.card.data.entities.*
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails

internal interface CardPaymentApi {

    @POST("device-data/{token}")
    suspend fun collectDeviceData(
        @Path("token") token: String,
        @Body payload: PaymentDetails,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): DeviceData

    @POST("payments/{token}")
    suspend fun processPaymentForFullCard(
        @Path("token") token: String,
        @Body payload: PaymentDetails,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): PaymentResponse

    @POST("payments/{token}/ThreeDSecureComplete")
    suspend fun processAuthorization(
        @Path("token") token: String,
        @Body payload: AuthorizationBody,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): PaymentResponse
    @POST("payments/recurring/{token}")
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

    @POST("payments/{token}/google-pay")
    suspend fun processGPay(
        @Path("token") token: String,
        @Body payload: GPayDetails
    ): PaymentResponse
}
