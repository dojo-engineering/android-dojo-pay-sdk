package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState

class CardCheckOutFullCardPaymentPayloadMapperTest {
    private val isStartDestination: Boolean = false
    private lateinit var mapper: CardCheckOutFullCardPaymentPayloadMapper

    @Before
    fun setUp() {
        mapper = CardCheckOutFullCardPaymentPayloadMapper()
    }

    @Test
    fun `when calling getPaymentPayLoad with email and billing enabled then should returns correct payload with email and billing address`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(true)
        given(currentState.emailInputField).willReturn(InputFieldState("test@example.com"))
        given(currentState.isBillingCountryFieldRequired).willReturn(true)
        given(currentState.isPostalCodeFieldRequired).willReturn(true)
        given(currentState.currentSelectedCountry).willReturn(
            SupportedCountriesViewEntity(
                countryCode = "US",
                countryName = "USA",
                isPostalCodeEnabled = false,
            ),
        )
        given(currentState.postalCodeField).willReturn(InputFieldState("12345"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = false,
                isChecked = true,
                messageText = "",
            ),
        )
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = true,
                messageText = "",
            ),
        )

        // Act
        val paymentPayload = mapper.getPaymentPayLoad(currentState, isStartDestination)

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
    fun `when calling getPaymentPayLoad with email enabled and billing is disabled then should returns correct payload with email but without billing address`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(true)
        given(currentState.emailInputField).willReturn(InputFieldState("test@example.com"))
        given(currentState.isBillingCountryFieldRequired).willReturn(false)
        given(currentState.isPostalCodeFieldRequired).willReturn(false)
        given(currentState.currentSelectedCountry).willReturn(
            SupportedCountriesViewEntity(
                countryCode = "US",
                countryName = "USA",
                isPostalCodeEnabled = false,
            ),
        )
        given(currentState.postalCodeField).willReturn(InputFieldState("12345"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = false,
                isChecked = true,
                messageText = "",
            ),
        )
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = true,
                messageText = "",
            ),
        )

        // Act

        val paymentPayload = mapper.getPaymentPayLoad(currentState, isStartDestination)

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
    fun `when calling getPaymentPayLoad with email and billing is disabled then should returns correct payload without email and billing address`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(false)
        given(currentState.isBillingCountryFieldRequired).willReturn(false)
        given(currentState.isPostalCodeFieldRequired).willReturn(false)
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = false,
                messageText = "",
            ),
        )

        // Act

        val paymentPayload = mapper.getPaymentPayLoad(currentState, isStartDestination)

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
    fun `when calling getPaymentPayLoad with valid month then should returns correct month`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(false)
        given(currentState.isBillingCountryFieldRequired).willReturn(false)
        given(currentState.isPostalCodeFieldRequired).willReturn(false)
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = false,
                messageText = "",
            ),
        )

        // Act

        val expiryMonth =
            mapper.getPaymentPayLoad(currentState, isStartDestination).cardDetails.expiryMonth

        // Assert
        assertEquals("12", expiryMonth)
    }

    @Test
    fun `when calling getPaymentPayLoad with blank month then should returns empty string`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(false)
        given(currentState.isBillingCountryFieldRequired).willReturn(false)
        given(currentState.isPostalCodeFieldRequired).willReturn(false)
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState(""))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = false,
                messageText = "",
            ),
        )

        // Act

        val expiryMonth =
            mapper.getPaymentPayLoad(currentState, isStartDestination).cardDetails.expiryMonth

        // Assert
        assertEquals("", expiryMonth)
    }

    @Test
    fun `when calling getPaymentPayLoad with valid year then should returns correct year`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(false)
        given(currentState.isBillingCountryFieldRequired).willReturn(false)
        given(currentState.isPostalCodeFieldRequired).willReturn(false)
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = false,
                messageText = "",
            ),
        )

        // Act

        val expiryYear =
            mapper.getPaymentPayLoad(currentState, isStartDestination).cardDetails.expiryYear

        // Assert
        assertEquals("25", expiryYear)
    }

    @Test
    fun `when calling getPaymentPayLoad with blank year then should returns empty string`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(false)
        given(currentState.isBillingCountryFieldRequired).willReturn(false)
        given(currentState.isPostalCodeFieldRequired).willReturn(false)
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState(""))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = false,
                messageText = "",
            ),
        )

        // Act

        val expiryYear =
            mapper.getPaymentPayLoad(currentState, isStartDestination).cardDetails.expiryYear

        // Assert
        assertEquals("", expiryYear)
    }

    @Test
    fun `when calling getPaymentPayLoad with isStartDestination as true then should returns correct payload with savePaymentMethod as null and mitConsentwhen as checkBox state`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(true)
        given(currentState.emailInputField).willReturn(InputFieldState("test@example.com"))
        given(currentState.isBillingCountryFieldRequired).willReturn(true)
        given(currentState.currentSelectedCountry).willReturn(
            SupportedCountriesViewEntity(
                countryCode = "US",
                countryName = "USA",
                isPostalCodeEnabled = false,
            ),
        )
        given(currentState.postalCodeField).willReturn(InputFieldState("12345"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.isPostalCodeFieldRequired).willReturn(true)
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState(""))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = true,
                isChecked = true,
                messageText = "",
            ),
        )

        // Act

        val paymentPayload = mapper.getPaymentPayLoad(
            currentState = currentState,
            isStartDestination = true,
        )

        // Assert
        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = "test@example.com",
            billingAddress = DojoAddressDetails(countryCode = "US", postcode = "12345"),
            savePaymentMethod = null,
            cardDetails = DojoCardDetails(
                cardNumber = "1234567812345678",
                cardName = "John Doe",
                expiryMonth = "",
                expiryYear = "",
                cv2 = "123",
                mitConsentGiven = true,
            ),
        )
        assertEquals(expectedPayload, paymentPayload)
    }

    @Test
    fun `when calling getPaymentPayLoad with isStartDestination as false and checkBox is inVisible then  should returns correct payload with savePaymentMethod as null`() {
        // Arrange
        val currentState: CardDetailsCheckoutState = mock()
        given(currentState.isEmailInputFieldRequired).willReturn(true)
        given(currentState.emailInputField).willReturn(InputFieldState("test@example.com"))
        given(currentState.isBillingCountryFieldRequired).willReturn(true)
        given(currentState.currentSelectedCountry).willReturn(
            SupportedCountriesViewEntity(
                countryCode = "US",
                countryName = "USA",
                isPostalCodeEnabled = false,
            ),
        )
        given(currentState.isPostalCodeFieldRequired).willReturn(true)
        given(currentState.postalCodeField).willReturn(InputFieldState("12345"))
        given(currentState.cardNumberInputField).willReturn(InputFieldState("1234567812345678"))
        given(currentState.cardHolderInputField).willReturn(InputFieldState("John Doe"))
        given(currentState.cardExpireDateInputField).willReturn(InputFieldState("1225"))
        given(currentState.cvvInputFieldState).willReturn(InputFieldState("123"))
        given(currentState.checkBoxItem).willReturn(
            CheckBoxItem(
                isVisible = false,
                isChecked = true,
                messageText = "",
            ),
        )

        // Act

        val paymentPayload = mapper.getPaymentPayLoad(
            currentState = currentState,
            isStartDestination = false,
        )

        // Assert
        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = "test@example.com",
            billingAddress = DojoAddressDetails(countryCode = "US", postcode = "12345"),
            savePaymentMethod = null,
            cardDetails = DojoCardDetails(
                cardNumber = "1234567812345678",
                cardName = "John Doe",
                expiryMonth = "12",
                expiryYear = "25",
                cv2 = "123",
                mitConsentGiven = null,
            ),
        )
        assertEquals(expectedPayload, paymentPayload)
    }
}
