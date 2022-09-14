package tech.dojo.pay.sdk.payemntintent.data

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentIntentApi {
    @GET("payment/{paymentId}")
    suspend fun fetchPaymentIntent(
        @Path("paymentId") paymentId: String
    ): Response<JsonObject>

    @POST("payment/{paymentId}/refresh-client-session-secret")
    suspend fun refreshPaymentIntent(
        @Path("paymentId") paymentId: String
    ): Response<JsonObject>
}
