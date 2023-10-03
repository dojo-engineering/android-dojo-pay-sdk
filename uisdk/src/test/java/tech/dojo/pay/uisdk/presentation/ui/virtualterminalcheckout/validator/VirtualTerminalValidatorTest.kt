package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldType
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator.TestData.billingAddress
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator.TestData.cardDetails
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator.TestData.shippingAddress

class VirtualTerminalValidatorTest {

    private val cardCheckoutScreenValidator: CardCheckoutScreenValidator = mock()

    private lateinit var validator: VirtualTerminalValidator

    @Before
    fun setup() {
        validator = VirtualTerminalValidator(cardCheckoutScreenValidator)
    }

    @Test
    fun `given calling validateEmailInputField with valid email then should return InputFieldState with emailFieldNewValue and false error`() {
        val emailFieldNewValue = "test@example.com"

        whenever(cardCheckoutScreenValidator.isEmailValid(emailFieldNewValue)).thenReturn(true)

        val result = validator.validateEmailInputField(emailFieldNewValue)

        val expected = InputFieldState(value = emailFieldNewValue, isError = false)

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateEmailInputField with invalid then should return InputFieldState with emailFieldNewValue , true error and error message`() {
        val emailFieldNewValue = "invalid-email"

        whenever(cardCheckoutScreenValidator.isEmailValid(emailFieldNewValue)).thenReturn(false)

        val result = validator.validateEmailInputField(emailFieldNewValue)

        val expected = InputFieldState(
            value = emailFieldNewValue,
            isError = true,
            errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_email,
        )

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateCardNumberInputField with valid CardNumber then should return InputFieldState with CardNumber new value  and false error`() {
        val cardNumberFieldNewValue = "4111111111111111"

        whenever(cardCheckoutScreenValidator.isCardNumberValid(cardNumberFieldNewValue)).thenReturn(true)

        val result = validator.validateCardNumberInputField(cardNumberFieldNewValue)

        val expected = InputFieldState(value = cardNumberFieldNewValue, isError = false)

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateCardNumberInputField with invalid card number then should return InputFieldState with CardNumber new value , true error and error message`() {
        val cardNumberFieldNewValue = "12345"

        whenever(cardCheckoutScreenValidator.isCardNumberValid(cardNumberFieldNewValue)).thenReturn(false)

        val result = validator.validateCardNumberInputField(cardNumberFieldNewValue)

        val expected = InputFieldState(
            value = cardNumberFieldNewValue,
            isError = true,
            errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_card_number,
        )

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateExpireDateInputField with valid ExpireDat then should return InputFieldState with ExpireDat new value and false error`() {
        val dateFieldNewValue = "1223"

        whenever(cardCheckoutScreenValidator.isCardExpireDateValid(dateFieldNewValue)).thenReturn(true)

        val result = validator.validateExpireDateInputField(dateFieldNewValue)

        val expected = InputFieldState(value = dateFieldNewValue, isError = false)

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateExpireDateInputField with invalid ExpireDat then should return InputFieldState with ExpireDat new value , true error and error message`() {
        val dateFieldNewValue = "123"

        whenever(cardCheckoutScreenValidator.isCardExpireDateValid(dateFieldNewValue)).thenReturn(false)

        val result = validator.validateExpireDateInputField(dateFieldNewValue)

        val expected = InputFieldState(
            value = dateFieldNewValue,
            isError = true,
            errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_expiry,
        )

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateCVVInputField with valid CVV then should return InputFieldState with CVV new value and false error`() {
        val cvvFieldNewValue = "123"

        whenever(cardCheckoutScreenValidator.isCvvValid(cvvFieldNewValue)).thenReturn(true)

        val result = validator.validateCVVInputField(cvvFieldNewValue)

        val expected = InputFieldState(value = cvvFieldNewValue, isError = false)

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateCVVInputField with invalid CVV then should return InputFieldState with CVV new value , true error and error message`() {
        val cvvFieldNewValue = "12"

        whenever(cardCheckoutScreenValidator.isCvvValid(cvvFieldNewValue)).thenReturn(false)

        val result = validator.validateCVVInputField(cvvFieldNewValue)

        val expected = InputFieldState(
            value = cvvFieldNewValue,
            isError = true,
            errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_cvv,
        )

        assertEquals(expected, result)
    }

    @Test
    fun`given calling validateInputFieldIsNotEmpty with valid input then should return InputFieldState with  new value and false error`() {
        val inputFieldValue = "Some value"
        val inputFieldType = InputFieldType.NAME

        val result = validator.validateInputFieldIsNotEmpty(inputFieldValue, inputFieldType)

        val expected = InputFieldState(value = inputFieldValue, isError = false)

        assertEquals(expected, result)
    }

    @Test
    fun `given calling validateInputFieldIsNotEmpty with invalid input then should return InputFieldState with new value and false error and error message `() {
        val inputFieldValue = ""
        val inputFieldType = InputFieldType.NAME

        val result = validator.validateInputFieldIsNotEmpty(inputFieldValue, inputFieldType)

        val expected = InputFieldState(
            value = inputFieldValue,
            isError = true,
            errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_shipping_name,
        )

        assertEquals(expected, result)
    }

    @Test
    fun `given calling isAllDataValid with valid input then should return true`() {
        val currentState: VirtualTerminalViewState = mock()
        whenever(currentState.cardDetailsSection).thenReturn(cardDetails)
        whenever(currentState.shippingAddressSection).thenReturn(shippingAddress)
        whenever(currentState.billingAddressSection).thenReturn(billingAddress)
        whenever(cardCheckoutScreenValidator.isCardNumberValid(Mockito.anyString())).thenReturn(true)
        whenever(cardCheckoutScreenValidator.isEmailValid(Mockito.anyString())).thenReturn(true)
        whenever(cardCheckoutScreenValidator.isCardExpireDateValid(Mockito.anyString())).thenReturn(true)
        whenever(cardCheckoutScreenValidator.isCvvValid(Mockito.anyString())).thenReturn(true)

        val result = validator.isAllDataValid(currentState)

        assertEquals(true, result)
    }

    @Test
    fun `given calling isAllDataValid with invalid input then should return false`() {
        val currentState: VirtualTerminalViewState = mock()
        whenever(currentState.cardDetailsSection).thenReturn(cardDetails)
        whenever(currentState.shippingAddressSection).thenReturn(shippingAddress)
        whenever(currentState.billingAddressSection).thenReturn(billingAddress)

        whenever(cardCheckoutScreenValidator.isCardNumberValid(Mockito.anyString())).thenReturn(false)
        whenever(cardCheckoutScreenValidator.isEmailValid(Mockito.anyString())).thenReturn(false)
        whenever(cardCheckoutScreenValidator.isCardExpireDateValid(Mockito.anyString())).thenReturn(false)
        whenever(cardCheckoutScreenValidator.isCvvValid(Mockito.anyString())).thenReturn(false)

        val result = validator.isAllDataValid(currentState)

        assertEquals(false, result)
    }
}

private object TestData {
    val cardDetails = CardDetailsViewState(
        isVisible = true,
        cardHolderInputField = InputFieldState(value = "John Doe", isError = false),
        cardNumberInputField = InputFieldState(value = "4111111111111111", isError = false),
        emailInputField = InputFieldState(value = "test@example.com", isError = false),
        cardExpireDateInputField = InputFieldState(value = "1223", isError = false),
        cvvInputFieldState = InputFieldState(value = "123", isError = false),
        allowedPaymentMethodsIcons = emptyList(),
    )
    val shippingAddress = ShippingAddressViewState(
        isVisible = true,
        addressLine1 = InputFieldState(value = "Address Line 1", isError = false),
        city = InputFieldState(value = "City", isError = false),
        postalCode = InputFieldState(value = "12345", isError = false),
        currentSelectedCountry = SupportedCountriesViewEntity("", "ShippingCountryCode", true),
        supportedCountriesList = emptyList(),
        name = InputFieldState("ShippingName"),
        addressLine2 = InputFieldState("ShippingAddressLine2"),
        deliveryNotes = InputFieldState("DeliveryNotes"),
        isShippingSameAsBillingCheckBox = CheckBoxItem(
            R.string.dojo_ui_sdk_card_details_checkout_billing_same_as_shipping,
            isChecked = false,
            isVisible = true,
        ),
    )

    val billingAddress = BillingAddressViewState(
        isVisible = true,
        currentSelectedCountry = SupportedCountriesViewEntity("", "BillingCountryCode", true),
        supportedCountriesList = emptyList(),
        addressLine2 = InputFieldState("BillingAddressLine2"),
        addressLine1 = InputFieldState(value = "Address Line 1", isError = false),
        city = InputFieldState(value = "City", isError = false),
        postalCode = InputFieldState(value = "12345", isError = false),
    )
}
