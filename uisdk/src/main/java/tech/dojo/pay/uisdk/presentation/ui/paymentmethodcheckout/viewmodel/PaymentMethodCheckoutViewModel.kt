package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.*
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.UpdateWalletState
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.presentation.components.AmountBreakDownItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayAmountButtonVState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCarButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import java.util.Currency

internal class PaymentMethodCheckoutViewModel(
    private val savedCardPaymentHandler: DojoSavedCardPaymentHandler,
    private val updateWalletState: UpdateWalletState,
    private val observePaymentIntent: ObservePaymentIntent,
    private val observePaymentMethods: ObservePaymentMethods,
    private val gpayPaymentHandler: DojoGPayHandler,
    private val gPayConfig: DojoGPayConfig?,
) : ViewModel() {
    private val mutableState = MutableLiveData<PaymentMethodCheckoutState>()
    val state: LiveData<PaymentMethodCheckoutState>
        get() = mutableState
    private lateinit var paymentIntent: PaymentIntentDomainEntity
    private var currentState: PaymentMethodCheckoutState
    private var isSavedCardsEmpty: Boolean = true

    init {
        currentState = PaymentMethodCheckoutState(
            gPayConfig = gPayConfig,
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = true,
            paymentMethodItem = null,
            amountBreakDownList = listOf(),
            totalAmount = "",
            cvvFieldState = InputFieldState(value = ""),
            payWithCarButtonState = PayWithCarButtonState(
                isVisibleL = false,
                isPrimary = false,
                navigateToCardCheckout = false
            ),
            payAmountButtonState = null
        )
        postStateToUI()
        observePaymentIntent()

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
                        if (currentState.isBottomSheetLoading) {
                            applyInitialFlowRules(it, gPayConfig)
                        }
                    }
                }
            }
        }
    }

    private fun applyInitialFlowRules(
        paymentIntentResult: PaymentIntentResult.Success,
        gPayConfig: DojoGPayConfig?
    ) {
        observePaymentMethods()
        if (paymentIntentResult.result.supportedWalletSchemes.contains(WalletSchemes.GOOGLE_PAY) && gPayConfig != null) {
            val gPayConfigWithSupportedCardsSchemes =
                gPayConfig.copy(allowedCardNetworks = paymentIntentResult.result.supportedCardsSchemes)
            currentState =
                currentState.copy(gPayConfig = gPayConfigWithSupportedCardsSchemes)
            postStateToUI()
        } else {
            handleGooglePayUnAvailable()
        }
    }

    private fun observePaymentMethods() {
        viewModelScope.launch {
            observePaymentMethods.observe().collect {
                isSavedCardsEmpty = if (it is FetchPaymentMethodsResult.Success) {
                    it.result.items.isEmpty()
                } else {
                    true
                }
            }
        }
    }

    private fun handleSuccessPaymentIntent(
        paymentIntentResult: PaymentIntentResult.Success,
        isGooglePayEnabled: Boolean
    ) {
        paymentIntent = paymentIntentResult.result
        updateWalletState(isGooglePayEnabled)
        currentState = PaymentMethodCheckoutState(
            gPayConfig = gPayConfig,
            isGooglePayButtonVisible = isGooglePayEnabled && gPayConfig != null && paymentIntent.supportedWalletSchemes.contains(
                WalletSchemes.GOOGLE_PAY
            ),
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            paymentMethodItem = if (isGooglePayEnabled && gPayConfig != null) PaymentMethodItemViewEntityItem.WalletItemItem else null,
            amountBreakDownList = getAmountBreakDownList() ?: emptyList(),
            totalAmount = Currency.getInstance(paymentIntent.amount.currencyCode).symbol +
                    paymentIntent.amount.valueString,

            payWithCarButtonState = getPayWithCarButtonStateWithGooglePayState(isGooglePayEnabled),
            payAmountButtonState = null,
            cvvFieldState = InputFieldState(value = ""),
        )
        postStateToUI()
    }

    private fun updateWalletState(isGooglePayEnabled: Boolean) {
        updateWalletState.updateWalletState(
            isGooglePayEnabled && gPayConfig != null && paymentIntent.supportedWalletSchemes.contains(
                WalletSchemes.GOOGLE_PAY
            )
        )
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

    private fun getPayWithCarButtonStateWithGooglePayState(
        isGooglePayEnabled: Boolean
    ): PayWithCarButtonState {
        return if (!isGooglePayEnabled || gPayConfig == null) {
            PayWithCarButtonState(
                isVisibleL = true,
                isPrimary = true,
                navigateToCardCheckout = isSavedCardsEmpty
            )
        } else {
            if (isSavedCardsEmpty) {
                PayWithCarButtonState(
                    isVisibleL = true,
                    isPrimary = false,
                    navigateToCardCheckout = true
                )

            } else {
                PayWithCarButtonState(
                    isVisibleL = false,
                    isPrimary = false,
                    navigateToCardCheckout = false
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
        gPayConfig?.let {
            val gPayConfigWithSupportedCardsSchemes =
                gPayConfig.copy(
                    allowedCardNetworks = paymentIntent.supportedCardsSchemes,
                    collectEmailAddress = paymentIntent.collectionEmailRequired,
                    collectBilling = paymentIntent.collectionEmailRequired
                )
            gpayPaymentHandler.executeGPay(
                GPayPayload = DojoGPayPayload(dojoGPayConfig = gPayConfigWithSupportedCardsSchemes),
                paymentIntent = DojoPaymentIntent(
                    token = paymentIntent.paymentToken,
                    totalAmount = DojoTotalAmount(
                        paymentIntent.amount.valueLong,
                        paymentIntent.amount.currencyCode
                    )
                )
            )
        }
    }

    fun onSavedPaymentMethodChanged(newValue: PaymentMethodItemViewEntityItem?) {
        if (newValue != currentState.paymentMethodItem) {
            currentState = currentState.copy(
                paymentMethodItem = newValue,
                payAmountButtonState = getPayAmountButtonState(newValue),
                payWithCarButtonState = PayWithCarButtonState(
                    isVisibleL = false,
                    isPrimary = false,
                    navigateToCardCheckout = false
                ),
                cvvFieldState = InputFieldState(""),
                isGooglePayButtonVisible = newValue is PaymentMethodItemViewEntityItem.WalletItemItem
            )
        }

        postStateToUI()
    }

    private fun getPayAmountButtonState(newValue: PaymentMethodItemViewEntityItem?): PayAmountButtonVState? {
        return if (newValue is PaymentMethodItemViewEntityItem.CardItemItem) {
            PayAmountButtonVState(isEnabled = currentState.cvvFieldState.value.length > 2)
        } else {
            null
        }
    }

    fun onCvvValueChanged(newValue: String) {
        currentState = currentState.copy(
            cvvFieldState = InputFieldState(value = newValue),
            payAmountButtonState = PayAmountButtonVState(newValue.length > 2)
        )
        postStateToUI()
    }

    fun onPayAmountClicked() {
        currentState = currentState.copy(
            payAmountButtonState = PayAmountButtonVState(
                currentState.cvvFieldState.value.length > 2,
                true
            )
        )
        savedCardPaymentHandler.executeSavedCardPayment(
            paymentIntent.paymentToken,
            DojoCardPaymentPayLoad.SavedCardPaymentPayLoad(
                cv2 = currentState.cvvFieldState.value,
                paymentMethodId = (currentState.paymentMethodItem as? PaymentMethodItemViewEntityItem.CardItemItem)?.id
                    ?: ""
            )
        )
    }

    private fun postStateToUI() {
        mutableState.postValue(currentState)
    }
}
