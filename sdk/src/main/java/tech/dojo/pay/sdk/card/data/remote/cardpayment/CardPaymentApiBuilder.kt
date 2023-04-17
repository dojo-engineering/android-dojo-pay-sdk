package tech.dojo.pay.sdk.card.data.remote.cardpayment

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.data.BaseUrlRepository
import java.util.concurrent.TimeUnit

internal class CardPaymentApiBuilder {

    fun create(): CardPaymentApi =
        createRetrofit().create(CardPaymentApi::class.java)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()

    private fun getBaseUrl(): String {
        val appCustomBaseUrl: String? = DojoSdk.dojoSDKDebugConfig.urlConfig?.connectE
        return if (!appCustomBaseUrl.isNullOrBlank()) {
            appCustomBaseUrl
        } else {
            BaseUrlRepository.getBaseUrl().ifEmpty { "https://web.e.connect.paymentsense.cloud/" }
        }
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
}
