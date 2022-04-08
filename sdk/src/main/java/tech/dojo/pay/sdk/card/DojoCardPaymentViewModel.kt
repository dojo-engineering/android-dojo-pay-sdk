package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import java.lang.Exception

internal class DojoCardPaymentViewModel(
    private val params: DojoCardPaymentParams,
    private val repository: CardPaymentRepository
) : ViewModel() {

    val result = MutableLiveData<DojoCardPaymentResult>()
    val threeDsNavigationEvent = MutableLiveData<Unit>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                repository.collectDeviceData()
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