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
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import java.util.Currency

internal class PaymentResultViewModel(
    private val result: DojoPaymentResult,
    private val observePaymentIntent: ObservePaymentIntent,
    private val refreshPaymentIntent: RefreshPaymentIntentUseCase,
    private val isDarkModeEnabled: Boolean
) : ViewModel() {
    private var currentState: PaymentResultState
    private var currentPaymentId: String = ""
    private val mutableState = MutableLiveData<PaymentResultState>()
    val state: LiveData<PaymentResultState>
        get() = mutableState

    init {
        currentState = buildInitStateWithPaymentResult(result)
        postStateToUi(currentState)
        viewModelScope.launch {
            observePaymentIntent.observePaymentIntent()
                .collect { it?.let { handlePaymentIntent(it) } }
        }
    }

    fun onTryAgainClicked() {
        currentState =
            (currentState as PaymentResultState.FailedResult).copy(isTryAgainLoading = true)
        postStateToUi(currentState)
        refreshPaymentIntent.refreshPaymentIntent(currentPaymentId)
        viewModelScope.launch {
            observePaymentIntent.observePaymentIntent()
                .collect { it?.let { handlePaymentIntent(it) } }
        }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            handlePaymentIntentSuccess(paymentIntentResult)
        } else if (paymentIntentResult is PaymentIntentResult.RefreshFailure) {
            handlePaymentIntentRefreshFailure()
        }
    }

    private fun handlePaymentIntentSuccess(paymentIntentResult: PaymentIntentResult.Success) {
        currentPaymentId = paymentIntentResult.result.id
        var state = if (result == DojoPaymentResult.SUCCESSFUL) {
            PaymentResultState.SuccessfulResult(
                appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_success,
                imageId = R.drawable.ic_success_circle,
                status = R.string.dojo_ui_sdk_payment_result_title_success,
                orderInfo = paymentIntentResult.result.orderId,
                description = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol +
                    paymentIntentResult.result.amount.valueString
            )
        } else {
            PaymentResultState.FailedResult(
                appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
                imageId = getErrorImage(),
                showTryAgain = true,
                status = R.string.dojo_ui_sdk_payment_result_title_fail,
                orderInfo = paymentIntentResult.result.orderId,
                isTryAgainLoading = false,
                shouldNavigateToPreviousScreen = false,
                details = R.string.dojo_ui_sdk_payment_result_failed_description
            )
        }
        if (currentState is PaymentResultState.FailedResult && (currentState as PaymentResultState.FailedResult).isTryAgainLoading) {
            state =
                (state as PaymentResultState.FailedResult).copy(shouldNavigateToPreviousScreen = true)
        }
        currentState = state
        postStateToUi(currentState)
    }

    private fun handlePaymentIntentRefreshFailure() {
        val state = PaymentResultState.FailedResult(
            appBarTitleId = R.string.dojo_ui_sdk_payment_result_title_fail,
            imageId = getErrorImage(),
            showTryAgain = true,
            isTryAgainLoading = false,
            shouldNavigateToPreviousScreen = false,
            status = R.string.dojo_ui_sdk_payment_result_title_fail,
            orderInfo = currentPaymentId,
            details = R.string.dojo_ui_sdk_payment_result_failed_description
        )
        currentState = state
        postStateToUi(currentState)
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
                imageId = getErrorImage(),
                showTryAgain = result != DojoPaymentResult.SDK_INTERNAL_ERROR,
                isTryAgainLoading = false,
                shouldNavigateToPreviousScreen = false,
                status = R.string.dojo_ui_sdk_payment_result_title_fail,
                orderInfo = "",
                details = R.string.dojo_ui_sdk_payment_result_failed_description
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
