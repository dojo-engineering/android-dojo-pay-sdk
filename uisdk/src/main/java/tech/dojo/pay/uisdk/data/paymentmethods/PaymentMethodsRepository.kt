package tech.dojo.pay.uisdk.data.paymentmethods

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import tech.dojo.pay.uisdk.data.entities.PaymentMethodsRaw
import tech.dojo.pay.uisdk.domain.entities.DeletePaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.mapper.SupportedPaymentMethodsDomainMapper

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class PaymentMethodsRepository(
    private val dataSource: PaymentMethodsDataSource = PaymentMethodsDataSource(),
    private val gson: Gson = Gson(),
    private val domainMapper: SupportedPaymentMethodsDomainMapper = SupportedPaymentMethodsDomainMapper(),

) {

    private  var fetchPaymentMethodResult: MutableStateFlow<FetchPaymentMethodsResult?> = MutableStateFlow(null)
    fun deletePaymentMethods(
        customerId: String,
        customerSecret: String,
        paymentMethodId: String,
        onDeletePaymentMethodsSuccess: (DeletePaymentMethodsResult) -> Unit,
        onDeletePaymentMethodsFailed: (DeletePaymentMethodsResult) -> Unit,
    ) {
        dataSource
            .deletePaymentMethods(
                customerId = customerId,
                customerSecret = customerSecret,
                paymentMethodId = paymentMethodId,
                onDeletePaymentMethodsSuccess = {
                    onDeletePaymentMethodsSuccess(
                        DeletePaymentMethodsResult.Success,
                    )
                },
                onDeletePaymentMethodsFailed = {
                    onDeletePaymentMethodsFailed(
                        DeletePaymentMethodsResult.Failure,
                    )
                },
            )
    }

    fun fetchPaymentMethod(customerId: String, customerSecret: String) {
        dataSource.fetchPaymentMethods(
            customerId,
            customerSecret,
            { handleSuccessPaymentMethod(it) },
            { fetchPaymentMethodResult.tryEmit(FetchPaymentMethodsResult.Failure) },
        )
    }

    fun observePaymentMethods() = fetchPaymentMethodResult
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
}
