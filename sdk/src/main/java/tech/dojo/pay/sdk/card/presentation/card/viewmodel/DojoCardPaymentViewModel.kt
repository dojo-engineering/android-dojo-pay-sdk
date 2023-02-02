package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.DeviceDataRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoCardPaymentViewModel(
    private val cardPaymentRepository: CardPaymentRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    private val deviceDataRepository: DeviceDataRepository,
    private val dojoCardPaymentPayLoad: DojoCardPaymentPayLoad,
    private val configuredCardinalInstance: Cardinal
) : Dojo3DSBaseViewModel(configuredCardinalInstance) {

    val paymentResult = MutableLiveData<PaymentResult>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                deviceData.value = deviceDataRepository.collectDeviceData(dojoCardPaymentPayLoad)
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
                paymentResult.value = cardPaymentRepository.processPayment()
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
        serverJWT: String? = null,
        transactionId: String? = null,
        validateResponse: ValidateResponse? = null
    ) {
        viewModelScope.launch {
            try {
                paymentResult.value =
                    dojo3DSRepository.processAuthorization(
                        serverJWT ?: "",
                        transactionId ?: "",
                        validateResponse
                    )
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
