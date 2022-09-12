package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import java.util.Currency

class PaymentResultViewModel(
    private val result: DojoPaymentResult,
    private val observePaymentIntent: ObservePaymentIntent
) : ViewModel() {
    private var currentState: PaymentResultState
    private val mutableState = MutableLiveData<PaymentResultState>()
    val state: LiveData<PaymentResultState>
        get() = mutableState

    init {
        currentState = buildInitStateWithPaymentResult(result)
        viewModelScope.launch {
            observePaymentIntent.observePaymentIntent()
                .collect { it?.let { handlePaymentIntent(it) } }
        }
        postStateToUi(currentState)
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            val state = if (result == DojoPaymentResult.SUCCESSFUL) {
                PaymentResultState.SuccessfulResult(
                    appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_success,
                    imageId = R.drawable.ic_success_circle,
                    status = R.string.dojo_ui_sdk_payment_result_title_success,
                    orderInfo = paymentIntentResult.result.id,
                    description = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol +
                            paymentIntentResult.result.amount.value
                )
            } else {
                PaymentResultState.FailedResult(
                    appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
                    imageId = R.drawable.ic_error_circle,
                    showTryAgain = result != DojoPaymentResult.SDK_INTERNAL_ERROR,
                    status = R.string.dojo_ui_sdk_payment_result_title_fail,
                    orderInfo = paymentIntentResult.result.id,
                    details = R.string.dojo_ui_sdk_payment_result_failed_description
                )
            }

            currentState = state
            postStateToUi(currentState)
        }
    }

    private fun buildInitStateWithPaymentResult(result: DojoPaymentResult) =
        if (result == DojoPaymentResult.SUCCESSFUL) {
            PaymentResultState.SuccessfulResult(
                appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_success,
                imageId = R.drawable.ic_success_circle,
                status = R.string.dojo_ui_sdk_payment_result_title_success,
                orderInfo = "",
                description = ""
            )
        } else {
            PaymentResultState.FailedResult(
                appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
                imageId = R.drawable.ic_error_circle,
                showTryAgain = result != DojoPaymentResult.SDK_INTERNAL_ERROR,
                status = R.string.dojo_ui_sdk_payment_result_title_fail,
                orderInfo = "",
                details = R.string.dojo_ui_sdk_payment_result_failed_description
            )
        }

    private fun postStateToUi(currentState: PaymentResultState) =
        mutableState.postValue(currentState)
}
