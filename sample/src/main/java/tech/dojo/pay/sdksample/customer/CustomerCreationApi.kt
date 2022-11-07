package tech.dojo.pay.sdksample.customer

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerCreationApi {

    @Headers(
        "Authorization:Basic sk_sandbox_b-pGLxFNm_kEr1aEWZcFp9HQu11wey7ucc48Y1e4-nZdGxhAJY3Bgx2Eb_C-itA16bSnojrZKesvnoZAoiRtPA",
        "Version: 2022-04-07",
        "Content-Type:application/json"
    )
    @POST("customers")
    suspend fun getCustomer(@Body params: CustomerRequest): CustomerResponse


    @Headers(
        "Authorization:Basic sk_sandbox_b-pGLxFNm_kEr1aEWZcFp9HQu11wey7ucc48Y1e4-nZdGxhAJY3Bgx2Eb_C-itA16bSnojrZKesvnoZAoiRtPA",
        "Version: 2022-04-07",
        "Content-Type:application/json"
    )
    @POST("customers/{customerId}/create-secret")
    suspend fun getCustomerSecrete(@Path("customerId") customerId: String): CustomerSecreteResponse
}
