package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import kotlin.Exception

internal class DojoCardPaymentViewModel(
    private val repository: CardPaymentRepository
) : ViewModel() {

    private val fingerPrintCapturedEvent = Channel<Unit>()
    val paymentResult = MutableLiveData<PaymentResult>()
    val threeDsPage = MutableLiveData<String>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                deviceData.value = repository.collectDeviceData()
                withTimeoutOrNull(FINGERPRINT_TIMEOUT_MILLIS) {
                    fingerPrintCapturedEvent.receive() //Wait till event is fired
                }
                paymentResult.value = repository.processPayment()
                canExit = true
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    fun onFingerprintCaptured() {
        fingerPrintCapturedEvent.trySend(Unit)
    }

    fun on3DSCompleted(result: DojoCardPaymentResult) {
        paymentResult.postValue(PaymentResult.Completed(result))
    }

    fun fetchThreeDsPage(params: ThreeDSParams) {
        viewModelScope.launch {
            try {
                threeDsPage.value = repository.fetch3dsPage(params)
            } catch (e: Exception) {
                paymentResult.value = PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    companion object {
        const val FINGERPRINT_TIMEOUT_MILLIS = 15000L
    }
}