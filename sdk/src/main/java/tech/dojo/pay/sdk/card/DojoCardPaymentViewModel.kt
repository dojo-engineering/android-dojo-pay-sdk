package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val params: DojoCardPaymentParams,
    private val repository: CardPaymentRepository
) : ViewModel() {

    val paymentResult = MutableLiveData<PaymentResult>()
    val threeDsPage = MutableLiveData<String>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        /*deviceData.value = DeviceData(
            formAction = "https://centinelapistag.cardinalcommerce.com/V1/Cruise/Collect",
            token = "token"
        )*/
        viewModelScope.launch {
            try {
                deviceData.value = repository.collectDeviceData(params.token, params.paymentPayload)
                delay(10000)
                paymentResult.value = repository.makePayment(params.token, params.paymentPayload)
                canExit = paymentResult.value is PaymentResult.ThreeDSRequired
            } catch (e: Exception) {
                paymentResult.value =
                    PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
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

    fun on3DSCompleted(result: DojoCardPaymentResult) {
        paymentResult.postValue(PaymentResult.Completed(result))
    }

}