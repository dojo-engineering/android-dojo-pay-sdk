package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator

import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldType
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState

internal class VirtualTerminalValidator(
    private val cardCheckoutScreenValidator: CardCheckoutScreenValidator
) {

    fun validateEmailInputField(emailFieldNewValue: String): InputFieldState {
        val isEmailValid: Boolean = cardCheckoutScreenValidator.isEmailValid(emailFieldNewValue)
        return if (emailFieldNewValue.isNotBlank() && !isEmailValid) {
            InputFieldState(
                value = emailFieldNewValue, isError = true, errorMessages = null
            )
        } else {
            validateInputFieldIsNotEmpty(emailFieldNewValue, InputFieldType.Email)
        }
    }

    fun validateCardNumberInputField(cardNumberFieldNewValue: String): InputFieldState {
        val isCardValid: Boolean =
            cardCheckoutScreenValidator.isCardNumberValid(cardNumberFieldNewValue)
        return if (cardNumberFieldNewValue.isNotBlank() && !isCardValid) {
            InputFieldState(
                value = cardNumberFieldNewValue, isError = true, errorMessages = null
            )
        } else {
            validateInputFieldIsNotEmpty(cardNumberFieldNewValue, InputFieldType.CARD_NUMBER)
        }
    }

    fun validateExpireDateInputField(dateFieldNewValue: String): InputFieldState {
        val isDateValid: Boolean =
            cardCheckoutScreenValidator.isCardExpireDateValid(dateFieldNewValue)
        return if (dateFieldNewValue.isNotBlank() && !isDateValid) {
            InputFieldState(
                value = dateFieldNewValue, isError = true, errorMessages = null
            )
        } else {
            validateInputFieldIsNotEmpty(dateFieldNewValue, InputFieldType.EXPIRE_DATA)
        }
    }

    fun validateCVVInputField(CVVFieldNewValue: String): InputFieldState {
        val isCVValid: Boolean = cardCheckoutScreenValidator.isCvvValid(CVVFieldNewValue)
        return if (CVVFieldNewValue.isNotBlank() && !isCVValid) {
            InputFieldState(
                value = CVVFieldNewValue, isError = true, errorMessages = null
            )
        } else {
            validateInputFieldIsNotEmpty(CVVFieldNewValue, InputFieldType.CVV)
        }
    }

    fun validateInputFieldIsNotEmpty(
        inPutFieldNewValue: String,
        type: InputFieldType
    ): InputFieldState {
        return if (isInPutFieldEmpty(inPutFieldNewValue)) {
            InputFieldState(
                value = inPutFieldNewValue,
                isError = true,
                errorMessages = getErrorMessageForEmptyInputField(type)
            )
        } else {
            InputFieldState(
                value = inPutFieldNewValue, isError = false
            )
        }
    }

    fun isAllDataValid(currentState: VirtualTerminalViewState): Boolean {
        val shippingAddressSection = currentState.shippingAddressSection
        val billingAddressSection = currentState.billingAddressSection
        val cardDetailsSection = currentState.cardDetailsSection
        return validateShippingAddressSection(shippingAddressSection) &&
            validateShippingAddressSection(billingAddressSection) &&
            validateCardDetailsSection(cardDetailsSection)
    }

    private fun validateShippingAddressSection(shippingAddressViewState: ShippingAddressViewState?): Boolean {
        return if (shippingAddressViewState?.isVisible == true) {
            shippingAddressViewState.name.value.isNotBlank() &&
                shippingAddressViewState.addressLine1.value.isNotBlank() &&
                shippingAddressViewState.city.value.isNotBlank() &&
                shippingAddressViewState.postalCode.value.isNotBlank()
        } else {
            true
        }
    }

    private fun validateShippingAddressSection(billingAddressSection: BillingAddressViewState?): Boolean {
        return if (billingAddressSection?.isVisible == true) {
            billingAddressSection.addressLine1.value.isNotBlank() &&
                billingAddressSection.city.value.isNotBlank() &&
                billingAddressSection.postalCode.value.isNotBlank()
        } else {
            true
        }
    }

    private fun validateCardDetailsSection(cardDetailsSection: CardDetailsViewState?): Boolean {
        return if (cardDetailsSection?.isVisible == true) {
            cardDetailsSection.cardHolderInputField.value.isNotBlank() &&
                cardCheckoutScreenValidator.isCardNumberValid(cardDetailsSection.cardNumberInputField.value) &&
                cardCheckoutScreenValidator.isEmailValid(cardDetailsSection.emailInputField.value) &&
                cardCheckoutScreenValidator.isCardExpireDateValid(cardDetailsSection.cardExpireDateInputField.value) &&
                cardCheckoutScreenValidator.isCvvValid(cardDetailsSection.cvvInputFieldState.value)
        } else {
            true
        }
    }

    private fun getErrorMessageForEmptyInputField(type: InputFieldType): Int {
        return R.string.dojo_ui_sdk_card_details_checkout_error_empty_billing_postcode
    }

    private fun isInPutFieldEmpty(inPutFieldValue: String): Boolean = inPutFieldValue.isBlank()
}
