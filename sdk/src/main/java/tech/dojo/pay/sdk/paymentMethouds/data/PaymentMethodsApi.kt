package tech.dojo.pay.sdk.paymentMethouds.data

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PaymentMethodsApi {
    @GET("customers/public/{customerId}/payment-methods")
    suspend fun fetchSavedPayment(
        @Path("customerId") customerId: String,
        @Header("Authorization") authorization: String,
        @Header("Version") version: String = API_VERSION
    ): Response<JsonObject>

    @DELETE("customers/public/{customerId}/payment-methods/{paymentMethodId}")
    suspend fun deleteSavedPayment(
        @Path("customerId") customerId: String,
        @Path("paymentMethodId") paymentMethodId: String,
        @Header("Authorization") authorization: String,
        @Header("Version") version: String = API_VERSION
    ): Response<JsonObject>
}

private const val API_VERSION = "2022-04-07"
