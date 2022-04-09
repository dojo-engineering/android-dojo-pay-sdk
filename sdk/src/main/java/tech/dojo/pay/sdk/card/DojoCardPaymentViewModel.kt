package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import java.lang.Exception

internal class DojoCardPaymentViewModel(
    private val params: DojoCardPaymentParams,
    private val repository: CardPaymentRepository
) : ViewModel() {

    val paymentResult = MutableLiveData<PaymentResult>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                paymentResult.value = repository.makePayment(params.token, params.paymentPayload)
                //canExit = paymentResult.value is PaymentResult.ThreeDSRequired
            } catch (e: Exception) {
                paymentResult.value =
                    PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    fun fetchThreeDsPage(params: ThreeDSParams) {
        viewModelScope.launch {
            paymentResult.value = repository.fetch3dsPage(params)
        }
    }

    fun on3DSCompleted() {
        paymentResult.value = PaymentResult.Completed(DojoCardPaymentResult.SUCCESSFUL)
    }

}