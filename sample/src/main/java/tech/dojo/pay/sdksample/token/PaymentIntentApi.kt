package tech.dojo.pay.sdksample.token

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaymentIntentApi {

    @Headers(
        "Authorization:Basic sk_sandbox_b-pGLxFNm_kEr1aEWZcFp9HQu11wey7ucc48Y1e4-nZdGxhAJY3Bgx2Eb_C-itA16bSnojrZKesvnoZAoiRtPA",
        "Version: 2022-02-07",
        "Content-Type:application/json"
    )
    @POST("payment-intents")
    suspend fun getToken(@Body params: PaymentIdBody): PaymentIdResponse
}
