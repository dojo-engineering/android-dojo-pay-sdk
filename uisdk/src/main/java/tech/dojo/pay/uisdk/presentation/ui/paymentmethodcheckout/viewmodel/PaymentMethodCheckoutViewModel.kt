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
import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.presentation.components.AmountBreakDownItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCarButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import java.util.*

class PaymentMethodCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val gpayPaymentHandler: DojoGPayHandler,
    private val gPayConfig: DojoGPayConfig?,
    private val isMangePaymentEnabled: Boolean
) : ViewModel() {
    private val mutableState = MutableLiveData<PaymentMethodCheckoutState>()
    val state: LiveData<PaymentMethodCheckoutState>
        get() = mutableState
    private lateinit var paymentIntent: PaymentIntentDomainEntity
    private var currentState: PaymentMethodCheckoutState

    init {
        currentState = PaymentMethodCheckoutState(
            isGooglePayVisible = false,
            isBottomSheetVisible = true,
            isLoading = true,
            isGpayItemVisible = false,
            amountBreakDownList = listOf(),
            totalAmount = "",
            payWithCarButtonState = PayWithCarButtonState(
                isVisibleL = false,
                isPrimary = false
            )
        )
        postStateToUI()
    }

    private suspend fun observePaymentIntentWithGooglePayState(isGooglePayEnabled: Boolean) {
        observePaymentIntent.observePaymentIntent().collect {
            it?.let {
                if (it is PaymentIntentResult.Success) {
                    handleSuccessPaymentIntent(it, isGooglePayEnabled)
                }
            }
        }
    }

    fun observePaymentIntent() {
        viewModelScope.launch {
            observePaymentIntent.observePaymentIntent().collect {
                it?.let {
                    if (it is PaymentIntentResult.Success) {
                        paymentIntent = it.result
                    }
                }
            }
        }
    }

    private fun handleSuccessPaymentIntent(
        paymentIntentResult: PaymentIntentResult.Success,
        isGooglePayEnabled: Boolean
    ) {
        paymentIntent = paymentIntentResult.result
        currentState = PaymentMethodCheckoutState(
            isGooglePayVisible = isGooglePayEnabled && gPayConfig != null,
            isBottomSheetVisible = true,
            isLoading = false,
            isGpayItemVisible = isMangePaymentEnabled,
            amountBreakDownList = getAmountBreakDownList() ?: emptyList(),
            totalAmount = Currency.getInstance(paymentIntent.amount.currencyCode).symbol +
                    paymentIntent.amount.valueString,

            payWithCarButtonState = getPayWithCarButtonState(isGooglePayEnabled)
        )
        postStateToUI()
    }

    private fun getAmountBreakDownList(): List<AmountBreakDownItem>? {
        return paymentIntent.itemLines?.map {
            AmountBreakDownItem(
                caption = it.caption,
                amount = Currency.getInstance(it.amount.currencyCode).symbol +
                        it.amount.value.centsToString()
            )
        }
    }

    private fun getPayWithCarButtonState(
        isGooglePayEnabled: Boolean
    ): PayWithCarButtonState {
        return if (!isGooglePayEnabled || gPayConfig == null) {
            PayWithCarButtonState(
                isVisibleL = true,
                isPrimary = true
            )
        } else {
            if (isMangePaymentEnabled) {
                PayWithCarButtonState(
                    isVisibleL = false,
                    isPrimary = true
                )
            } else {
                PayWithCarButtonState(
                    isVisibleL = true,
                    isPrimary = false
                )
            }
        }
    }

    fun handleGooglePayAvailable() {
        viewModelScope.launch { observePaymentIntentWithGooglePayState(isGooglePayEnabled = true) }
    }

    fun handleGooglePayUnAvailable() {
        viewModelScope.launch { observePaymentIntentWithGooglePayState(isGooglePayEnabled = false) }
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
                token = paymentIntent.paymentToken,
                totalAmount = DojoTotalAmount(10, "GBP")
            )
        )
    }

    private fun postStateToUI() {
        mutableState.postValue(currentState)
    }
}
