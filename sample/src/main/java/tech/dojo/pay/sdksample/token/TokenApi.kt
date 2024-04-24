package tech.dojo.pay.sdksample.token

import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {

    @POST("access-tokens")
    suspend fun getToken(@Body params: MerchantParams): TokenResponse
}
