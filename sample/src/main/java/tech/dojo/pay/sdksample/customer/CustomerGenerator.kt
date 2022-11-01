package tech.dojo.pay.sdksample.customer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CustomerGenerator {
    private val CustomerCreationApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.dojo.tech/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CustomerCreationApi::class.java)
    }

    suspend fun generateCustomerId(): CustomerResponse = CustomerCreationApi.getCustomer(
        CustomerRequest(
            name = "mahmoud",
            emailAddress = "samy@dojo.com"
        )
    )
}