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
                appBarTitleId = R.string.dojo_payment_result_text_payment_complete,
                imageId = R.drawable.ic_success_circle,
                status = result.name,
                orderInfo = result.name,
                description = result.name
            )
        } else {
            PaymentResultState.FailedResult(
                appBarTitleId = R.string.dojo_payment_result_text_payment_failed,
                imageId = R.drawable.ic_error_circle,
                showTryAgain = result != DojoPaymentResult.SDK_INTERNAL_ERROR,
                status = result.name,
                details = result.name
            )
        }
        mutableState.postValue(resultState)
    }
}
