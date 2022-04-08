package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import java.lang.Exception

internal class DojoCardPaymentViewModel(
    private val params: DojoCardPaymentParams,
    private val repository: CardPaymentRepository
) : ViewModel() {

    val events = MutableLiveData<DojoCardPaymentEvent>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                val deviceData = repository.collectDeviceData(params.token, params.paymentPayload)
                canExit = true
                threeDsNavigationEvent.value = Unit
            } catch (e: Exception) {
                result.value = DojoCardPaymentResult.SDK_INTERNAL_ERROR
            }
        }
    }

    fun on3DSCompleted() {
        result.value = DojoCardPaymentResult.SUCCESSFUL
    }

}