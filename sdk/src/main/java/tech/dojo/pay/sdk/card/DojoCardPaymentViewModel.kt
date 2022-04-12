package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import kotlin.Exception

internal class DojoCardPaymentViewModel(
    private val repository: CardPaymentRepository
) : ViewModel() {

    val paymentResult = MutableLiveData<PaymentResult>()
    val threeDsPage = MutableLiveData<String>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        launchCatching {
            deviceData.value = repository.collectDeviceData()
        }
    }

    fun onFingerprintCaptured() {
        launchCatching {
            paymentResult.value = repository.processPayment()
            canExit = true
        }
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

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch {
            try {
                block()
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
            }
        }

}