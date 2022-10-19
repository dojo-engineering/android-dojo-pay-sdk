package tech.dojo.pay.sdksample.token

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.dojo.pay.uisdk.data.entities.BillingAddress
import tech.dojo.pay.uisdk.data.entities.CustomerEmail

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
            config = Config(
                customerEmail = CustomerEmail(true),
                billingAddress = BillingAddress(true)
            ),
            itemLines = listOf(
                ItemLines(
                    amountTotal = Amount(
                        10L,
                        currencyCode = "GBP"
                    ),
                    caption = "Pixel 5"
                ),
                ItemLines(
                    amountTotal = Amount(
                        10L,
                        currencyCode = "GBP"
                    ),
                    caption = "Pixel 6"
                ),
                ItemLines(
                    amountTotal = Amount(
                        10L,
                        currencyCode = "GBP"
                    ),
                    caption = "Pixel 6 pro"
                ),
            ),
            reference = "Order 234",
            description = "Demo payment intent"
        )
    ).id
}
