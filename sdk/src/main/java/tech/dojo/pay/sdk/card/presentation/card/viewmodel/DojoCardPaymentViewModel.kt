package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.presentation.threeds.CardinalConfigurator
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoCardPaymentViewModel(
    private val repository: CardPaymentRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    private val cardinalConfigurator: CardinalConfigurator
) : Dojo3DSBaseViewModel() {

    private val fingerPrintCapturedEvent = Channel<Unit>()
    val paymentResult = MutableLiveData<PaymentResult>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                deviceData.value = repository.collectDeviceData()
                paymentResult.value = repository.processPayment()
                canExit = true
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    fun onFingerprintCaptured() {
        fingerPrintCapturedEvent.trySend(Unit)
    }

    override fun on3DSCompleted(result: DojoPaymentResult) {
        paymentResult.postValue(PaymentResult.Completed(result))
    }

    override fun fetchThreeDsPage(params: ThreeDSParams) {
        viewModelScope.launch {
            try {
                threeDsPage.value = dojo3DSRepository.fetch3dsPage(params)
            } catch (e: Exception) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    companion object {
        const val FINGERPRINT_TIMEOUT_MILLIS = 15000L
    }
}
