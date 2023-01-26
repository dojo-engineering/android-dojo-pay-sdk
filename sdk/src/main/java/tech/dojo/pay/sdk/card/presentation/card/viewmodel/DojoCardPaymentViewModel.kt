package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoCardPaymentViewModel(
    private val repository: CardPaymentRepository,
    configuredCardinalInstance: Cardinal
) : Dojo3DSBaseViewModel(configuredCardinalInstance) {

    val paymentResult = MutableLiveData<PaymentResult>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                deviceData.value = repository.collectDeviceData()
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    fun initCardinal() {
        configureDCardinalInstance.init(deviceData.value?.token, this)
    }

    override fun onSetupCompleted(consumerSessionId: String?) {
        viewModelScope.launch {
            try {
                paymentResult.value = repository.processPayment()
                canExit = true
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    override fun onValidated(validateResponse: ValidateResponse?, serverJwt: String?) {
        postPaymentFieldToUI()
    }

    fun on3dsCompleted(
        validateResponse: ValidateResponse? = null,
        serverJWT: String? = null
    ) {
        viewModelScope.launch {
            try {
                paymentResult.value = repository.processAuthorization(serverJWT ?: "")
                canExit = true
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    private fun postPaymentFieldToUI() {
        paymentResult.value = PaymentResult.Completed(DojoPaymentResult.FAILED)
    }
}
