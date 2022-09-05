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
            isLoading = false
        )
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
        viewModelScope.launch { observePaymentStatus() }
    }

    fun onCardHolderValueChanged(newValue: String) {
        pushStateToUi(
            currentState.copy(
                cardHolderInputField = InputFieldState(
                    labelStringId = R.string.dojo_ui_sdk_card_details_checkout_field_card_name,
                    value = newValue,
                )
            )
        )
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        when (paymentIntentResult) {
            is PaymentIntentResult.Success -> {
                paymentToken = paymentIntentResult.result.paymentToken
                currentState = CardDetailsCheckoutState(
                    totalAmount = paymentIntentResult.result.amount.value.toString(),
                    amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
                    cardHolderInputField = InputFieldState(
                        labelStringId = R.string.dojo_ui_sdk_card_details_checkout_field_card_name,
                        value = "",
                    ),
                    isLoading = false
                )
                pushStateToUi(currentState)
            }
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
                cardNumber = "4456530000001096",
                cardName = "Card holder",
                expiryMonth = "12",
                expiryYear = "24",
                cv2 = "020"
            )
        )
}
