package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.threeds.CardinalConfigurator
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoCardPaymentViewModel(
    private val repository: CardPaymentRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    cardinalConfigurator: CardinalConfigurator
) : Dojo3DSBaseViewModel(cardinalConfigurator) {

    private val fingerPrintCapturedEvent = Channel<Unit>()
    val paymentResult = MutableLiveData<PaymentResult>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                deviceData.value = repository.collectDeviceData()
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    fun initCardinal() { configureDCardinalInstance.init(deviceData.value?.token, this) }

    override fun onSetupCompleted(consumerSessionId: String?) {
        viewModelScope.launch {
            try {
                paymentResult.value = repository.processPayment()
                canExit = true
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }

    override fun onValidated(validateResponse: ValidateResponse?, serverJwt: String?) {
        paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
    }

    override fun onValidated(
        context: Context?,
        validateResponse: ValidateResponse?,
        serverJWT: String?
    ) {
        viewModelScope.launch {
            try {
                paymentResult.value = repository.processAuthorization(serverJWT ?: "")
                canExit = true
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }


    fun onFingerprintCaptured() {
        fingerPrintCapturedEvent.trySend(Unit)
    }

    companion object {
        const val FINGERPRINT_TIMEOUT_MILLIS = 15000L
    }
}
