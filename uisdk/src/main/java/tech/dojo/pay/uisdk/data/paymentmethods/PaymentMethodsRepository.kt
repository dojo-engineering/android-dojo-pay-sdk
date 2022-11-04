package tech.dojo.pay.uisdk.data.paymentmethods

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import tech.dojo.pay.uisdk.data.entities.PaymentMethodsRaw
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.mapper.SupportedPaymentMethodsDomainMapper

internal class PaymentMethodsRepository(
    private val dataSource: PaymentMethodsDataSource = PaymentMethodsDataSource(),
    private val gson: Gson = Gson(),
    private val domainMapper: SupportedPaymentMethodsDomainMapper = SupportedPaymentMethodsDomainMapper()

) {

    private lateinit var fetchPaymentMethodResult: MutableStateFlow<FetchPaymentMethodsResult?>

    fun fetchPaymentMethod(customerId: String, customerSecret: String) {
        fetchPaymentMethodResult = MutableStateFlow(null)
        dataSource.fetchPaymentMethods(
            customerId,
            customerSecret,
            { handleSuccessPaymentMethod(it) },
            { fetchPaymentMethodResult.tryEmit(FetchPaymentMethodsResult.Failure) }
        )
    }

    private fun handleSuccessPaymentMethod(successPaymentMethod: String) {
        try {
            val domainEntity = domainMapper.apply(mapToPaymentIntentPayLoad(successPaymentMethod))
            fetchPaymentMethodResult.tryEmit(FetchPaymentMethodsResult.Success(domainEntity))
        } catch (e: Exception) {
            fetchPaymentMethodResult.tryEmit(FetchPaymentMethodsResult.Failure)
        }
    }

    private fun mapToPaymentIntentPayLoad(paymentMethodPayloadJson: String) =
        gson.fromJson(paymentMethodPayloadJson, PaymentMethodsRaw::class.java)

    fun observePaymentMethods() = fetchPaymentMethodResult
}
