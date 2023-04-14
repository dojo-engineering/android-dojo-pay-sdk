package tech.dojo.pay.sdk.card.data.remote.baseurl

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tech.dojo.pay.sdk.DojoSdk
import java.util.concurrent.TimeUnit

internal object BaseUrlApiBuilder {
    fun create(): BaseUrlApi =
        createRetrofit().create(BaseUrlApi::class.java)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getApiBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()

    private fun getApiBaseUrl() = BASE_URL_GOOGLE_PROD

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
}

private const val BASE_URL_GOOGLE_PROD =
    "https://storage.googleapis.com/remote-ag-prod-manifest/"
