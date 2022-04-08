package tech.dojo.pay.sdk.card.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardPaymentApiBuilder(
    private val sandboxMode: Boolean,
    private val token: String
) {

    fun create(): CardPaymentApi =
        createRetrofit().create(CardPaymentApi::class.java)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getBaseUrl(sandboxMode))
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()

    private fun getBaseUrl(sandboxMode: Boolean): String {
        val extaPart = if (sandboxMode) "test." else ""
        return "https://web.e.${extaPart}connect.paymentsense.cloud/api/"
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(token))
            .build()

    class TokenInterceptor(private val token: String) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest = request
                .newBuilder()
                .url("https://www.google.com")
                //.method("${request.method()}/$token", request.body())
                .build()
            return chain.proceed(newRequest)
        }
    }
}