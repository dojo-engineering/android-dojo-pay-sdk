package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState

class CardCheckOutFullCardPaymentPayloadMapperTest {

    @Test
    fun `given calling getPaymentPayLoad with email and billing enabled should returns correct payload with email and billing address`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.isEmailInputFieldRequired } returns true
        every { currentState.emailInputField.value } returns "test@example.com"
        every { currentState.isBillingCountryFieldRequired } returns true
        every { currentState.currentSelectedCountry.countryCode } returns "US"
        every { currentState.isPostalCodeFieldRequired } returns true
        every { currentState.postalCodeField.value } returns "12345"
        every { currentState.checkBoxItem.isChecked } returns true
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cardExpireDateInputField.value } returns "1225"
        every { currentState.cvvInputFieldState.value } returns "123"

        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val paymentPayload = mapper.getPaymentPayLoad(currentState)

        // Assert
        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = "test@example.com",
            billingAddress = DojoAddressDetails(countryCode = "US", postcode = "12345"),
            savePaymentMethod = true,
            cardDetails = DojoCardDetails(
                cardNumber = "1234567812345678",
                cardName = "John Doe",
                expiryMonth = "12",
                expiryYear = "25",
                cv2 = "123",
            ),
        )
        assertEquals(expectedPayload, paymentPayload)
    }

    @Test
    fun `given calling getPaymentPayLoad with email enabled and billing is disabled should returns correct payload with email but without billing address`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.isEmailInputFieldRequired } returns true
        every { currentState.emailInputField.value } returns "test@example.com"
        every { currentState.isBillingCountryFieldRequired } returns false
        every { currentState.isPostalCodeFieldRequired } returns false
        every { currentState.postalCodeField.value } returns "12345"
        every { currentState.checkBoxItem.isChecked } returns true
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cardExpireDateInputField.value } returns "1225"
        every { currentState.cvvInputFieldState.value } returns "123"
        every { currentState.currentSelectedCountry } returns SupportedCountriesViewEntity(
            "",
            "",
            false,
        )

        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val paymentPayload = mapper.getPaymentPayLoad(currentState)

        // Assert
        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = "test@example.com",
            billingAddress = DojoAddressDetails(
                null,
                null,
            ), // Billing address is not required, so it should be null
            savePaymentMethod = true,
            cardDetails = DojoCardDetails(
                cardNumber = "1234567812345678",
                cardName = "John Doe",
                expiryMonth = "12",
                expiryYear = "25",
                cv2 = "123",
            ),
        )
        assertEquals(expectedPayload, paymentPayload)
    }

    @Test
    fun `given calling getPaymentPayLoad with email and billing is disabled should  returns correct payload without email and billing address`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.isEmailInputFieldRequired } returns false
        every { currentState.isBillingCountryFieldRequired } returns false
        every { currentState.isPostalCodeFieldRequired } returns false
        every { currentState.checkBoxItem.isChecked } returns false
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cardExpireDateInputField.value } returns "1225"
        every { currentState.cvvInputFieldState.value } returns "123"

        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val paymentPayload = mapper.getPaymentPayLoad(currentState)

        // Assert
        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = null,
            billingAddress = DojoAddressDetails(null, null),
            savePaymentMethod = false,
            cardDetails = DojoCardDetails(
                cardNumber = "1234567812345678",
                cardName = "John Doe",
                expiryMonth = "12",
                expiryYear = "25",
                cv2 = "123",
            ),
        )
        assertEquals(expectedPayload, paymentPayload)
    }

    @Test
    fun `given calling getPaymentPayLoad with valid month should returns correct month`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.isEmailInputFieldRequired } returns false
        every { currentState.isBillingCountryFieldRequired } returns false
        every { currentState.isPostalCodeFieldRequired } returns false
        every { currentState.checkBoxItem.isChecked } returns false
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cardExpireDateInputField.value } returns "1225"
        every { currentState.cvvInputFieldState.value } returns "123"

        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val expiryMonth = mapper.getPaymentPayLoad(currentState).cardDetails.expiryMonth

        // Assert
        assertEquals("12", expiryMonth)
    }

    @Test
    fun `given calling getPaymentPayLoad with blank month should returns empty string`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.cardExpireDateInputField.value } returns ""
        every { currentState.isEmailInputFieldRequired } returns false
        every { currentState.isBillingCountryFieldRequired } returns false
        every { currentState.isPostalCodeFieldRequired } returns false
        every { currentState.checkBoxItem.isChecked } returns false
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cvvInputFieldState.value } returns "123"
        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val expiryMonth = mapper.getPaymentPayLoad(currentState).cardDetails.expiryMonth

        // Assert
        assertEquals("", expiryMonth)
    }

    @Test
    fun `given calling getPaymentPayLoad with valid year should returns correct year`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.cardExpireDateInputField.value } returns "1225"
        every { currentState.isEmailInputFieldRequired } returns false
        every { currentState.isBillingCountryFieldRequired } returns false
        every { currentState.isPostalCodeFieldRequired } returns false
        every { currentState.checkBoxItem.isChecked } returns false
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cvvInputFieldState.value } returns "123"
        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val expiryYear = mapper.getPaymentPayLoad(currentState).cardDetails.expiryYear

        // Assert
        assertEquals("25", expiryYear)
    }

    @Test
    fun `given calling getPaymentPayLoad with blank year should returns empty string`() {
        // Arrange
        val currentState = mockk<CardDetailsCheckoutState>()
        every { currentState.cardExpireDateInputField.value } returns ""
        every { currentState.isEmailInputFieldRequired } returns false
        every { currentState.isBillingCountryFieldRequired } returns false
        every { currentState.isPostalCodeFieldRequired } returns false
        every { currentState.checkBoxItem.isChecked } returns false
        every { currentState.cardNumberInputField.value } returns "1234567812345678"
        every { currentState.cardHolderInputField.value } returns "John Doe"
        every { currentState.cvvInputFieldState.value } returns "123"

        // Act
        val mapper = CardCheckOutFullCardPaymentPayloadMapper()
        val expiryYear = mapper.getPaymentPayLoad(currentState).cardDetails.expiryYear

        // Assert
        assertEquals("", expiryYear)
    }
}
