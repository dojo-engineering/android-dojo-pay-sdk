package tech.dojo.pay.sdksample.token

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TokenGenerator {

    private val tokenApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://e.test.connect.paymentsense.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenApi::class.java)
    }

    suspend fun generateToken(): String = tokenApi.getToken(
        MerchantParams(
            gatewayUsername = "connect-e-test-3ds1-api",
            currencyCode = "826",
            amount = "100",
            transactionType = "SALE",
            orderId = "ORD00001",
            orderDescription = "Example description."
        )
    ).id
}
