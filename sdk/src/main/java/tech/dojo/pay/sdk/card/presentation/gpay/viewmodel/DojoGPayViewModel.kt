package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult

internal class DojoGPayViewModel(
    private val repository: GPayRepository,
    private val gpayPaymentRequestMapper: GpayPaymentRequestMapper
) : ViewModel() {
    val paymentResult = MutableLiveData<PaymentResult>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    fun sendGPayDataToServer(gPayData: String, dojoGPayParams: DojoGPayParams) {
        viewModelScope.launch {
            try {
                val result = repository.processPayment(
                    gpayPaymentRequestMapper.apply(
                        gPayData,
                        dojoGPayParams
                    )
                )
                paymentResult.value = result
                canExit = true
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }
}