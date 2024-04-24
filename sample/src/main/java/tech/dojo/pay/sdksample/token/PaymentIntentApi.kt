package tech.dojo.pay.sdksample.token

import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentIntentApi {

    @POST("payment-intents")
    suspend fun getToken(@Body params: PaymentIdBody): PaymentIdResponse
}
