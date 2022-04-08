package tech.dojo.pay.sdk.card.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class CardPaymentApiBuilder(
    private val sandboxMode: Boolean
) {

    fun create(): CardPaymentApi =
        createRetrofit().create(CardPaymentApi::class.java)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrl(sandboxMode))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun getBaseUrl(sandboxMode: Boolean): String {
        val extaPart = if (sandboxMode) "test." else ""
        return "https://web.e.${extaPart}connect.paymentsense.cloud/api/"
    }
}