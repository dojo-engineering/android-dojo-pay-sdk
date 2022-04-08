package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

internal class DojoCardPaymentViewModel(
    private val params: DojoCardPaymentParams
) : ViewModel() {

    val events = MutableLiveData<DojoCardPaymentEvent>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            delay(3000) //Make requests
            canExit = true
            events.value = DojoCardPaymentEvent.Navigate3DS
        }
    }

    fun on3DSCompleted() {
        events.value = DojoCardPaymentEvent.ReturnResult(DojoCardPaymentResult.SUCCESSFUL)
    }

}