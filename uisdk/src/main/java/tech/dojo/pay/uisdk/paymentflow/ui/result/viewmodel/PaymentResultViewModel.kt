package tech.dojo.pay.uisdk.paymentflow.ui.result.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.paymentflow.ui.result.state.PaymentResultState

class PaymentResultViewModel(
    private val result: DojoPaymentResult
) : ViewModel() {
    private val mutableState = MutableLiveData<PaymentResultState>()
    val state: LiveData<PaymentResultState>
        get() = mutableState

    init {
        mutableState.postValue(
            PaymentResultState(
                imageId = getResultImage(),
                status = result.name,
                orderInfo = result.name,
                description = result.name
            )
        )
    }

    private fun getResultImage() = if (result == DojoPaymentResult.SUCCESSFUL) {
        R.drawable.ic_success_circle
    } else {
        R.drawable.ic_error_circle
    }
}
