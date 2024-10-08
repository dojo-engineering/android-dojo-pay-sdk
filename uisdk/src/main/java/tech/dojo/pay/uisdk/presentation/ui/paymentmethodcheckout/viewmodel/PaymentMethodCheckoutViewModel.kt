package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.domain.IsWalletAvailableFromDeviceAndIntentUseCase
import tech.dojo.pay.uisdk.domain.MakeGpayPaymentUseCase
import tech.dojo.pay.uisdk.domain.MakeSavedCardPaymentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.MakeGpayPaymentParams
import tech.dojo.pay.uisdk.domain.entities.MakeSavedCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.mapper.PaymentMethodCheckoutViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayAmountButtonVState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCardButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState

internal class PaymentMethodCheckoutViewModel(
    private var savedCardPaymentHandler: DojoSavedCardPaymentHandler,
    private val observePaymentIntent: ObservePaymentIntent,
    private val observePaymentMethods: ObservePaymentMethods,
    private var gpayPaymentHandler: DojoGPayHandler,
    private val gPayConfig: DojoGPayConfig?,
    private val observePaymentStatus: ObservePaymentStatus,
    private val viewEntityMapper: PaymentMethodCheckoutViewEntityMapper,
    private val makeGpayPaymentUseCase: MakeGpayPaymentUseCase,
    private val makeSavedCardPaymentUseCase: MakeSavedCardPaymentUseCase,
    private val isWalletAvailableFromDeviceAndIntentUseCase: IsWalletAvailableFromDeviceAndIntentUseCase,
    private val navigateToCardResult: (dojoPaymentResult: DojoPaymentResult) -> Unit,
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
        payWithCardButtonState = PayWithCardButtonState(
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
        observePaymentStatus()
        observeGooglePayPaymentState()
    }

    private fun emitLoadingToView() {
        postStateToUI()
    }
    private fun observePaymentStatus() {
        viewModelScope.launch {
            observePaymentStatus
                .observePaymentStates()
                .drop(1)
                .collect {
                    currentState = currentState.copy(
                        payAmountButtonState = currentState.payAmountButtonState?.copy(isLoading = it),
                    )
                    postStateToUI()
                }
        }
    }
    private fun observeGooglePayPaymentState() {
        viewModelScope.launch {
            observePaymentStatus
                .observeGpayPaymentStates()
                .drop(1)
                .collect {
                    currentState = currentState.copy(isBottomSheetLoading = it)
                    postStateToUI()
                }
        }
    }

    private fun buildViewStateWithDateFlows() {
        viewModelScope.launch {
            combine(
                observePaymentIntent.observePaymentIntent(),
                observePaymentMethods.observe(),
            ) { paymentIntentResult, paymentMethods ->
                if (paymentIntentResult is PaymentIntentResult.Success && paymentMethods !is FetchPaymentMethodsResult.Fetching) {
                    paymentIntent = paymentIntentResult.result
                    currentState = viewEntityMapper.mapToViewState(
                        paymentMethods,
                        isWalletAvailableFromDeviceAndIntentUseCase.isAvailable(),
                        paymentIntentResult,
                    )
                }
            }.collectLatest { postStateToUI() }
        }
    }

    fun onGpayCLicked() {
        gPayConfig?.let {
            viewModelScope.launch {
                makeGpayPaymentUseCase.makePaymentWithUpdatedToken(
                    params = MakeGpayPaymentParams(
                        dojoGPayConfig = gPayConfig.copy(
                            allowedCardNetworks = paymentIntent.supportedCardsSchemes,
                            collectEmailAddress = paymentIntent.collectionEmailRequired,
                            collectBilling = paymentIntent.collectionEmailRequired,
                        ),
                        dojoTotalAmount = DojoTotalAmount(
                            paymentIntent.totalAmount.valueLong,
                            paymentIntent.totalAmount.currencyCode,
                        ),
                        gpayPaymentHandler = gpayPaymentHandler,
                        paymentId = paymentIntent.id,
                    ),
                    onError = { navigateToCardResult(DojoPaymentResult.SDK_INTERNAL_ERROR) },
                )
            }
        }
    }

    fun onSavedPaymentMethodChanged(newValue: PaymentMethodItemViewEntityItem?) {
        currentState = currentState.copy(
            cvvFieldState = InputFieldState(value = ""),
        )
        if (newValue is PaymentMethodItemViewEntityItem.NoItem) {
            currentState = currentState.copy(
                paymentMethodItem = null,
                payWithCardButtonState = PayWithCardButtonState(
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
                    payWithCardButtonState = PayWithCardButtonState(
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
        viewModelScope.launch {
            makeSavedCardPaymentUseCase
                .makePaymentWithUpdatedToken(
                    params = MakeSavedCardPaymentParams(
                        savedCardPaymentHandler = savedCardPaymentHandler,
                        cv2 = currentCvvValue,
                        paymentId = paymentIntent.id,
                        paymentMethodId = (currentState.paymentMethodItem as? PaymentMethodItemViewEntityItem.CardItemItem)?.id
                            ?: "",
                    ),
                    onError = { navigateToCardResult(DojoPaymentResult.SDK_INTERNAL_ERROR) },
                )
        }
    }
    private fun postStateToUI() {
        mutableState.postValue(currentState)
    }
}
