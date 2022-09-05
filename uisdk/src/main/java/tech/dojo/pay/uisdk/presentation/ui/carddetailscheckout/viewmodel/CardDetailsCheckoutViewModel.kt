package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsInputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import java.util.Currency

class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val observePaymentStatus: ObservePaymentStatus,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase
) : ViewModel() {
    private lateinit var paymentToken: String
    private var currentState: CardDetailsCheckoutState
    private val mutableState = MutableLiveData<CardDetailsCheckoutState>()
    val state: LiveData<CardDetailsCheckoutState>
        get() = mutableState

    init {
        currentState = CardDetailsCheckoutState(
            totalAmount = "",
            amountCurrency = "",
            cardHolderInputField = InputFieldState(
                labelStringId = R.string.dojo_ui_sdk_card_details_checkout_field_card_name,
                value = "",
            ),
            cardDetailsInPutField = CardDetailsInputFieldState(
                inputFieldLabel = R.string.dojo_ui_sdk_card_details_checkout_field_pan,
                cardNumberLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan,
                cardNumberValue = "",
                cvvLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv,
                cvvValue = "",
                expireDateLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry,
                expireDateValueValue = "",
            ),
            isLoading = false
        )
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
        viewModelScope.launch { observePaymentStatus() }
    }

    fun onCardHolderValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardHolderInputField = InputFieldState(
                labelStringId = R.string.dojo_ui_sdk_card_details_checkout_field_card_name,
                value = newValue,
            )
        )
        pushStateToUi(currentState)
    }

    fun onCardNumberValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardDetailsInPutField = CardDetailsInputFieldState(
                inputFieldLabel = R.string.dojo_ui_sdk_card_details_checkout_field_pan,
                cardNumberLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan,
                cardNumberValue = newValue,
                cvvLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv,
                cvvValue = currentState.cardDetailsInPutField.cvvValue,
                expireDateLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry,
                expireDateValueValue = currentState.cardDetailsInPutField.expireDateValueValue,
            )
        )
        pushStateToUi(currentState)
    }

    fun onCvvValueChanged(newValue: String) {
        currentState =
            currentState.copy(
                cardDetailsInPutField = CardDetailsInputFieldState(
                    inputFieldLabel = R.string.dojo_ui_sdk_card_details_checkout_field_pan,
                    cardNumberLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan,
                    cardNumberValue = currentState.cardDetailsInPutField.cardNumberValue,
                    cvvLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv,
                    cvvValue = newValue,
                    expireDateLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry,
                    expireDateValueValue = currentState.cardDetailsInPutField.expireDateValueValue,
                )
            )
        pushStateToUi(currentState)
    }

    fun onExpireDareValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardDetailsInPutField = CardDetailsInputFieldState(
                inputFieldLabel = R.string.dojo_ui_sdk_card_details_checkout_field_pan,
                cardNumberLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan,
                cardNumberValue = currentState.cardDetailsInPutField.cardNumberValue,
                cvvLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv,
                cvvValue = currentState.cardDetailsInPutField.cvvValue,
                expireDateLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry,
                expireDateValueValue = newValue,
            )
        )
        pushStateToUi(currentState)
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            paymentToken = paymentIntentResult.result.paymentToken
            currentState = CardDetailsCheckoutState(
                totalAmount = paymentIntentResult.result.amount.value.toString(),
                amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
                cardHolderInputField = InputFieldState(
                    labelStringId = R.string.dojo_ui_sdk_card_details_checkout_field_card_name,
                    value = "",
                ),
                cardDetailsInPutField = CardDetailsInputFieldState(
                    inputFieldLabel = R.string.dojo_ui_sdk_card_details_checkout_field_pan,
                    cardNumberLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan,
                    cardNumberValue = "",
                    cvvLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv,
                    cvvValue = "",
                    expireDateLabel = R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry,
                    expireDateValueValue = "",
                ),
                isLoading = false
            )
            pushStateToUi(currentState)

        }
    }

    private suspend fun observePaymentStatus() {
        observePaymentStatus.observePaymentStates().collect {
            if (!it) {
                pushStateToUi(currentState.copy(isLoading = false))
            }
        }
    }

    fun onPayWithCardClicked() {
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        pushStateToUi(currentState.copy(isLoading = true))
        dojoCardPaymentHandler.executeCardPayment(
            paymentToken,
            getPaymentPayLoad()
        )
    }

    private fun pushStateToUi(state: CardDetailsCheckoutState) {
        mutableState.value = state
    }

    private fun getPaymentPayLoad(): DojoCardPaymentPayLoad.FullCardPaymentPayload =
        DojoCardPaymentPayLoad.FullCardPaymentPayload(
            DojoCardDetails(
                cardNumber = currentState.cardDetailsInPutField.cardNumberValue,
                cardName = currentState.cardHolderInputField.value,
                expiryMonth = currentState.cardDetailsInPutField.expireDateValueValue.substring(0,2),
                expiryYear = currentState.cardDetailsInPutField.expireDateValueValue.substring(2,4),
                cv2 = currentState.cardDetailsInPutField.cvvValue
            )
        )
}
