package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.mapper.PaymentMethodCheckoutViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayAmountButtonVState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCarButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState

internal class PaymentMethodCheckoutViewModel(
    private var savedCardPaymentHandler: DojoSavedCardPaymentHandler,
    private val observePaymentIntent: ObservePaymentIntent,
    private val observePaymentMethods: ObservePaymentMethods,
    private var gpayPaymentHandler: DojoGPayHandler,
    private val gPayConfig: DojoGPayConfig?,
    private val observePaymentStatus: ObservePaymentStatus,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val observeWalletState: ObserveWalletState,
    private val viewEntityMapper: PaymentMethodCheckoutViewEntityMapper,
) : ViewModel() {
    private val mutableState = MutableLiveData<PaymentMethodCheckoutState>()
    val state: LiveData<PaymentMethodCheckoutState>
        get() = mutableState
    private lateinit var paymentIntent: PaymentIntentDomainEntity
    private var currentState = PaymentMethodCheckoutState(
        isGooglePayButtonVisible = false,
        isBottomSheetVisible = true,
        isBottomSheetLoading = true,
        paymentMethodItem = null,
        amountBreakDownList = listOf(),
        totalAmount = "",
        cvvFieldState = InputFieldState(value = ""),
        payWithCarButtonState = PayWithCarButtonState(
            isVisible = false,
            isPrimary = false,
            navigateToCardCheckout = false,
        ),
        payAmountButtonState = null,
    )
    private var currentCvvValue: String = ""

    fun updateSavedCardPaymentHandler(newSavedCardPaymentHandler: DojoSavedCardPaymentHandler) {
        savedCardPaymentHandler = newSavedCardPaymentHandler
    }

    fun updateGpayHandler(newGpayPaymentHandler: DojoGPayHandler) {
        gpayPaymentHandler = newGpayPaymentHandler
    }

    init {
        emitLoadingToView()
        buildViewStateWithDateFlows()
    }

    private fun emitLoadingToView() { postStateToUI() }

    private fun buildViewStateWithDateFlows() {
        viewModelScope.launch {
            val observePaymentIntentFlow = observePaymentIntent.observePaymentIntent()
            val observePaymentMethodsFlow = observePaymentMethods.observe()
            val observeWalletStateFlow = observeWalletState.observe()
            combine(
                observePaymentIntentFlow,
                observePaymentMethodsFlow,
                observeWalletStateFlow,
            ) { paymentIntentResult, paymentMethods, walletState ->
                if (paymentIntentResult is PaymentIntentResult.Success && walletState != null) {
                    paymentIntent = paymentIntentResult.result
                    currentState = viewEntityMapper.mapToViewState(
                        paymentMethods,
                        walletState,
                        paymentIntentResult,
                    )
                }
            }.collectLatest { postStateToUI() }
        }
    }

    fun onGpayCLicked() {
        gPayConfig?.let {
            updatePaymentStateUseCase.updateGpayPaymentSate(true)
            val gPayConfigWithSupportedCardsSchemes =
                gPayConfig.copy(
                    allowedCardNetworks = paymentIntent.supportedCardsSchemes,
                    collectEmailAddress = paymentIntent.collectionEmailRequired,
                    collectBilling = paymentIntent.collectionEmailRequired,
                )
            gpayPaymentHandler.executeGPay(
                GPayPayload = DojoGPayPayload(dojoGPayConfig = gPayConfigWithSupportedCardsSchemes),
                paymentIntent = DojoPaymentIntent(
                    token = paymentIntent.paymentToken,
                    totalAmount = DojoTotalAmount(
                        paymentIntent.amount.valueLong,
                        paymentIntent.amount.currencyCode,
                    ),
                ),
            )
            currentState = currentState.copy(isBottomSheetLoading = true)
            viewModelScope.launch {
                delay(500)
                postStateToUI()
            }
            observeGooglePayPaymentState()
        }
    }

    private fun observeGooglePayPaymentState() {
        viewModelScope.launch {
            observePaymentStatus.observeGpayPaymentStates().collect {
                currentState = currentState.copy(isBottomSheetLoading = it)
                postStateToUI()
            }
        }
    }

    fun onSavedPaymentMethodChanged(newValue: PaymentMethodItemViewEntityItem?) {
        observePaymentStatus()
        currentState = currentState.copy(
            cvvFieldState = InputFieldState(value = ""),
        )
        if (newValue is PaymentMethodItemViewEntityItem.NoItem) {
            currentState = currentState.copy(
                paymentMethodItem = null,
                payWithCarButtonState = PayWithCarButtonState(
                    isVisible = true,
                    isPrimary = !currentState.isGooglePayButtonVisible,
                    navigateToCardCheckout = true,
                ),
                payAmountButtonState = null,
            )
        } else {
            if (newValue != currentState.paymentMethodItem) {
                currentState = currentState.copy(
                    paymentMethodItem = newValue,
                    payAmountButtonState = getPayAmountButtonState(newValue),
                    payWithCarButtonState = PayWithCarButtonState(
                        isVisible = false,
                        isPrimary = false,
                        navigateToCardCheckout = false,
                    ),
                    isGooglePayButtonVisible = newValue is PaymentMethodItemViewEntityItem.WalletItemItem,
                )
            }
        }
        postStateToUI()
    }

    private fun observePaymentStatus() {
        viewModelScope.launch {
            observePaymentStatus.observePaymentStates().collect {
                currentState = currentState.copy(
                    payAmountButtonState = currentState.payAmountButtonState?.copy(isLoading = it),
                )
                postStateToUI()
            }
        }
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
            payAmountButtonState = PayAmountButtonVState(newValue.length > 2),
        )
        currentCvvValue = newValue
        postStateToUI()
    }

    fun onPayAmountClicked() {
        updatePaymentStateUseCase.updatePaymentSate(true)
        currentState = currentState.copy(
            payAmountButtonState = PayAmountButtonVState(
                isEnabled = true,
                isLoading = true,
            ),
        )
        postStateToUI()
        savedCardPaymentHandler.executeSavedCardPayment(
            paymentIntent.paymentToken,
            DojoCardPaymentPayLoad.SavedCardPaymentPayLoad(
                cv2 = currentCvvValue,
                paymentMethodId = (currentState.paymentMethodItem as? PaymentMethodItemViewEntityItem.CardItemItem)?.id
                    ?: "",
            ),
        )
    }

    private fun postStateToUI() {
        mutableState.postValue(currentState)
    }
}
