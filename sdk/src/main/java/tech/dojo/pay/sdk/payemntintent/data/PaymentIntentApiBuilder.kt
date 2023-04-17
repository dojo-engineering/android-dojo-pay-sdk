package tech.dojo.pay.sdk.payemntintent.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tech.dojo.pay.sdk.DojoSdk
import java.util.concurrent.TimeUnit

internal class PaymentIntentApiBuilder {
    fun create(): PaymentIntentApi =
        createRetrofit().create(PaymentIntentApi::class.java)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()

    private fun getBaseUrl() = if (!DojoSdk.dojoSDKDebugConfig.urlConfig?.remote.isNullOrBlank()) {
        DojoSdk.dojoSDKDebugConfig.urlConfig?.remote ?: ""
    } else {
        "https://api.dojo.tech/"
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
}
