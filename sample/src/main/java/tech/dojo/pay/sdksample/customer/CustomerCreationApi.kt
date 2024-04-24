package tech.dojo.pay.sdksample.customer

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerCreationApi {

    @POST("customers")
    suspend fun getCustomer(@Body params: CustomerRequest): CustomerResponse

    @POST("customers/{customerId}/create-secret")
    suspend fun getCustomerSecrete(@Path("customerId") customerId: String): CustomerSecreteResponse
}
