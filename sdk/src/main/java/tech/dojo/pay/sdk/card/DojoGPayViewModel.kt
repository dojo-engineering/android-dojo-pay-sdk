package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.entities.PaymentResult

internal class DojoGPayViewModel(
    private val repository: GPayRepository
) : ViewModel() {

    val paymentResult = MutableLiveData<PaymentResult>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    fun sendGPayDataToServer(gPayData: String) {
        viewModelScope.launch {
            try {
                var gPayPayload = GPayDetails(
                    token = gPayData,
                    userEmailAddress = null,
                    userPhoneNumber = null,
                    billingAddress = null,
                    shippingDetails = null,
                    metaData = null
                )
                var a = repository.processPayment(gPayPayload)
                canExit = true
            } catch (throwable: Throwable) {
                var a = 0
//                paymentResult.value = PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }
}