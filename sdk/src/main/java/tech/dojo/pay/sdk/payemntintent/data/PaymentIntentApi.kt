package tech.dojo.pay.sdk.payemntintent.data

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentIntentApi {

    @GET("payment-intents/public/{paymentId}")
    suspend fun fetchPaymentIntent(
        @Path("paymentId") paymentId: String,
        @Header("Version") version: String = API_VERSION,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): Response<JsonObject>

    @GET("payment-intents/public/{paymentId}")
    suspend fun fetchSetUpIntent(
        @Path("paymentId") paymentId: String,
        @Header("Version") version: String = API_VERSION,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): Response<JsonObject>

    @POST("payment-intents/public/{paymentId}/refresh-client-session-secret")
    suspend fun refreshPaymentIntent(
        @Path("paymentId") paymentId: String,
        @Header("Version") version: String = API_VERSION,
        @Header("IS-MOBILE") isMobile: Boolean = true,
    ): Response<JsonObject>
}

private const val API_VERSION = "2022-04-07"
