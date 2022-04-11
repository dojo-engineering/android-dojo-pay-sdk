package tech.dojo.pay.sdk.card.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

internal class CardPaymentApiBuilder(
    private val sandboxMode: Boolean
) {

    fun create(): CardPaymentApi =
        createRetrofit().create(CardPaymentApi::class.java)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrl(sandboxMode))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()

    private fun getBaseUrl(sandboxMode: Boolean): String {
        val extaPart = if (sandboxMode) "test." else ""
        return "https://web.e.${extaPart}connect.paymentsense.cloud/api/"
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
}