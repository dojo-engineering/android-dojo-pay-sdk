package tech.dojo.pay.sdksample.token

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PaymentIDGenerator {
    private val paymentIntentApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.dojo.tech/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentIntentApi::class.java)
    }

    suspend fun generatePaymentId(): String = paymentIntentApi.getToken(
        PaymentIdBody(
            amount = Amount(
                value = 10L,
                currencyCode = "GBP"
            ),
            reference = "Order 234",
            description = "Demo payment intent"
        )
    ).id
}
