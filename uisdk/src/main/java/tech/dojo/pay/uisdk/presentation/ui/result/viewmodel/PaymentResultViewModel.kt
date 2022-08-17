package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState

class PaymentResultViewModel(
    result: DojoPaymentResult
) : ViewModel() {
    private val mutableState = MutableLiveData<PaymentResultState>()
    val state: LiveData<PaymentResultState>
        get() = mutableState

    init {
        val resultState = if (result == DojoPaymentResult.SUCCESSFUL) {
            PaymentResultState.SuccessfulResult(
                imageId = R.drawable.ic_success_circle,
                status = result.name,
                orderInfo = result.name,
                description = result.name
            )
        } else {
            PaymentResultState.FailedResult(
                imageId = R.drawable.ic_error_circle,
                status = result.name,
                details = result.name
            )
        }
        mutableState.postValue(resultState)
    }

}
