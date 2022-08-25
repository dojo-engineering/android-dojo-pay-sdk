package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState

class PaymentMethodCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val gpayPaymentHandler: DojoGPayHandler
) : ViewModel() {
    private val mutableState = MutableLiveData<PaymentMethodCheckoutState>()
    val state: LiveData<PaymentMethodCheckoutState>
        get() = mutableState
    private lateinit var paymentToken: String

    init {
        viewModelScope.launch { observePaymentIntent() }
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect {
            it?.let {
                when (it) {
                    is PaymentIntentResult.Success -> paymentToken = it.result.clientSessionSecret
                }
            }
        }
    }

    fun handleGooglePayAvailable() {
        mutableState.postValue(
            PaymentMethodCheckoutState(
                isGooglePayVisible = true,
                isBottomSheetVisible = true
            )
        )
    }

    fun handleGooglePayUnAvailable() {
        mutableState.postValue(
            PaymentMethodCheckoutState(
                isGooglePayVisible = false,
                isBottomSheetVisible = true
            )
        )
    }

    fun onGpayCLicked() {
        gpayPaymentHandler.executeGPay(
            GPayPayload = DojoGPayPayload(
                DojoGPayConfig(
                    merchantName = "Dojo Cafe (Paymentsense)",
                    merchantId = "BCR2DN6T57R5ZI34",
                    gatewayMerchantId = "119784244252745"
                )
            ),
            paymentIntent = DojoPaymentIntent(
                token = paymentToken,
                totalAmount = DojoTotalAmount(10, "GBP")
            )
        )
    }
}
