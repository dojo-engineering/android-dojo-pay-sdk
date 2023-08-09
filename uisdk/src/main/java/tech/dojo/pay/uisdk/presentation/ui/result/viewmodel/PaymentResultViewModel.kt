package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState

internal class PaymentResultViewModel(
    result: DojoPaymentResult,
    private val isDarkModeEnabled: Boolean,
    private val stringProvider: StringProvider,
) : ViewModel() {
    private var currentState: PaymentResultState
    private val mutableState = MutableLiveData<PaymentResultState>()
    val state: LiveData<PaymentResultState>
        get() = mutableState

    init {
        currentState = buildInitStateWithPaymentResult(result)
        postStateToUi(currentState)
    }

    fun onTryAgainClicked() {
        currentState =
            (currentState as PaymentResultState.FailedResult).copy(shouldNavigateToPreviousScreen = true)
        postStateToUi(currentState)
    }

    private fun buildInitStateWithPaymentResult(result: DojoPaymentResult) =
        if (result == DojoPaymentResult.SUCCESSFUL) {
            PaymentResultState.SuccessfulResult(
                appBarTitle = stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_success),
                imageId = R.drawable.ic_success_circle,
                status = stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_success),
                orderInfo = "",
                description = "",
            )
        } else {
            PaymentResultState.FailedResult(
                appBarTitle = stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_fail),
                imageId = getErrorImage(),
                isTryAgainLoading = false,
                shouldNavigateToPreviousScreen = false,
                status = stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_fail),
                orderInfo = "",
                details = R.string.dojo_ui_sdk_payment_result_failed_description,
            )
        }

    private fun getErrorImage() = if (isDarkModeEnabled) {
        R.drawable.ic_error_dark
    } else {
        R.drawable.ic_error_circle
    }

    private fun postStateToUi(currentState: PaymentResultState) =
        mutableState.postValue(currentState)
}
