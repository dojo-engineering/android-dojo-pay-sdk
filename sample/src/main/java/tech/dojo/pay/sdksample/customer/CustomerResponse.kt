package tech.dojo.pay.sdksample.customer

data class CustomerResponse(
    val id: String,
    val name: String,
    val emailAddress: String
)
data class CustomerRequest(
    val name:String,
    val emailAddress:String
)