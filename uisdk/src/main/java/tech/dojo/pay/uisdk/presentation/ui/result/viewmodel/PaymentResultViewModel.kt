package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.ui.result.mapper.PaymentResultViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState

internal class PaymentResultViewModel(
    result: DojoPaymentResult,
    private val observePaymentIntent: ObservePaymentIntent,
    private val paymentResultViewEntityMapper: PaymentResultViewEntityMapper,
) : ViewModel() {
    private var currentState: PaymentResultState
    private val mutableState = MutableLiveData<PaymentResultState>()
    val state: LiveData<PaymentResultState>
        get() = mutableState

    init {
        currentState = paymentResultViewEntityMapper.mapTpResultState(result)
        postStateToUi(currentState)
        viewModelScope.launch {
            observePaymentIntent
                .observePaymentIntent()
                .collectLatest {
                    if (it is PaymentIntentResult.Success && currentState is PaymentResultState.SuccessfulResult) {
                        currentState =
                            (currentState as PaymentResultState.SuccessfulResult).copy(
                                orderInfo = paymentResultViewEntityMapper.mapToOrderIdField(
                                    it.result.orderId,
                                ),
                            )
                        postStateToUi(currentState)
                    }
                }
        }
    }

    fun onTryAgainClicked() {
        if (currentState is PaymentResultState.FailedResult) {
            currentState =
                (currentState as PaymentResultState.FailedResult).copy(shouldNavigateToPreviousScreen = true)
            postStateToUi(currentState)
        }
    }

    private fun postStateToUi(currentState: PaymentResultState) =
        mutableState.postValue(currentState)
}
