package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.PaymentResult

@Suppress("SwallowedException")
internal class DojoVirtualTerminalViewModel(
    private val cardPaymentRepository: CardPaymentRepository,
) : ViewModel() {
    val paymentResult = MutableLiveData<PaymentResult>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                paymentResult.postValue(cardPaymentRepository.processPayment())
                canExit = true
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    private fun postPaymentFieldToUI() {
        paymentResult.postValue(PaymentResult.Completed(DojoPaymentResult.FAILED))
        canExit = true
    }
}
