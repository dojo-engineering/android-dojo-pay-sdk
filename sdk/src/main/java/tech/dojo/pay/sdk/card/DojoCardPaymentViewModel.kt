package tech.dojo.pay.sdk.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

class DojoCardPaymentViewModel(
    private val payload: DojoCardPaymentPayload
) : ViewModel() {

    val result = MutableLiveData<DojoCardPaymentResult>()

    init {
        viewModelScope.launch {
            delay(3000)
            result.postValue(DojoCardPaymentResult.SUCCESSFUL)
        }
    }

}