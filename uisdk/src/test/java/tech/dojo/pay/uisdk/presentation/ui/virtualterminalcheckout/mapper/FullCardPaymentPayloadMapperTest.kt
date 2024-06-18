package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestData.billingAddress
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestData.cardDetails
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestData.shippingAddress
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState

class FullCardPaymentPayloadMapperTest {

    private lateinit var mapper: FullCardPaymentPayloadMapper

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mapper = FullCardPaymentPayloadMapper()
    }

    @Test
    fun `given calling apply from the mapper with different billing and shipping address should map field correctly`() {
        // Set up mock data
        val currentState: VirtualTerminalViewState = mock()
        whenever(currentState.cardDetailsSection).thenReturn(cardDetails)
        whenever(currentState.shippingAddressSection).thenReturn(shippingAddress)
        whenever(currentState.billingAddressSection).thenReturn(billingAddress)

        // Expected results
        val expectedCardDetails = DojoCardDetails(
            cardNumber = "CardNumber",
            cardName = "CardHolder",
            expiryMonth = "MM",
            expiryYear = "YY",
            cv2 = "CVV",
        )

        val expectedShippingDetails = DojoShippingDetails(
            name = "ShippingName",
            address = DojoAddressDetails(
                address1 = "ShippingAddressLine1",
                address2 = "ShippingAddressLine2",
                city = "ShippingCity",
                postcode = "ShippingPostalCode",
                countryCode = "ShippingCountryCode",
            ),
        )

        val expectedBillingAddress = DojoAddressDetails(
            address1 = "BillingAddressLine1",
            address2 = "BillingAddressLine2",
            city = "BillingCity",
            postcode = "BillingPostalCode",
            countryCode = "BillingCountryCode",
        )

        val expectedMetaData = mapOf("DeliveryNotes" to "DeliveryNotes")

        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            cardDetails = expectedCardDetails,
            shippingDetails = expectedShippingDetails,
            userEmailAddress = "email",
            billingAddress = expectedBillingAddress,
            metaData = expectedMetaData,
        )

        // Call the method under test
        val result = mapper.apply(currentState)

        // Verify the expected payload
        assertEquals(expectedPayload, result)
    }

    @Test
    fun `given calling apply from the mapper with same billing and shipping address should map address from shipping `() {
        // Set up mock data
        val currentState: VirtualTerminalViewState = mock()
        whenever(currentState.cardDetailsSection).thenReturn(cardDetails)
        whenever(currentState.shippingAddressSection).thenReturn(
            shippingAddress.updateIsShippingSameAsBillingCheckBox(
                CheckBoxItem(
                    R.string.dojo_ui_sdk_card_details_checkout_billing_same_as_shipping,
                    isChecked = true,
                    isVisible = true,
                ),
            ),
        )
        whenever(currentState.billingAddressSection).thenReturn(billingAddress)

        // Expected results
        val expectedCardDetails = DojoCardDetails(
            cardNumber = "CardNumber",
            cardName = "CardHolder",
            expiryMonth = "MM",
            expiryYear = "YY",
            cv2 = "CVV",
        )

        val expectedShippingDetails = DojoShippingDetails(
            name = "ShippingName",
            address = DojoAddressDetails(
                address1 = "ShippingAddressLine1",
                address2 = "ShippingAddressLine2",
                city = "ShippingCity",
                postcode = "ShippingPostalCode",
                countryCode = "ShippingCountryCode",
            ),
        )

        val expectedBillingAddress = DojoAddressDetails(
            address1 = "ShippingAddressLine1",
            address2 = "ShippingAddressLine2",
            city = "ShippingCity",
            postcode = "ShippingPostalCode",
            countryCode = "ShippingCountryCode",
        )

        val expectedMetaData = mapOf("DeliveryNotes" to "DeliveryNotes")

        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            cardDetails = expectedCardDetails,
            shippingDetails = expectedShippingDetails,
            userEmailAddress = "email",
            billingAddress = expectedBillingAddress,
            metaData = expectedMetaData,
        )

        // Call the method under test
        val result = mapper.apply(currentState)

        // Verify the expected payload
        assertEquals(expectedPayload, result)
    }

    @Test
    fun `given calling apply from the mapper with false visible shipping address should map shipping as null `() {
        // Set up mock data
        val currentState: VirtualTerminalViewState = mock()
        whenever(currentState.cardDetailsSection).thenReturn(cardDetails)
        whenever(currentState.shippingAddressSection).thenReturn(shippingAddress.updateIsVisible(false))
        whenever(currentState.billingAddressSection).thenReturn(billingAddress)

        // Expected results
        val expectedCardDetails = DojoCardDetails(
            cardNumber = "CardNumber",
            cardName = "CardHolder",
            expiryMonth = "MM",
            expiryYear = "YY",
            cv2 = "CVV",
        )

        val expectedShippingDetails = null

        val expectedBillingAddress = DojoAddressDetails(
            address1 = "BillingAddressLine1",
            address2 = "BillingAddressLine2",
            city = "BillingCity",
            postcode = "BillingPostalCode",
            countryCode = "BillingCountryCode",
        )

        val expectedMetaData = mapOf("DeliveryNotes" to "DeliveryNotes")

        val expectedPayload = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            cardDetails = expectedCardDetails,
            shippingDetails = expectedShippingDetails,
            userEmailAddress = "email",
            billingAddress = expectedBillingAddress,
            metaData = expectedMetaData,
        )

        // Call the method under test
        val result = mapper.apply(currentState)

        // Verify the expected payload
        assertEquals(expectedPayload, result)
    }
}

private object TestData {
    val cardDetails = CardDetailsViewState(
        isVisible = true,
        emailInputField = InputFieldState("email"),
        cardHolderInputField = InputFieldState("CardHolder"),
        cardNumberInputField = InputFieldState("CardNumber"),
        cardExpireDateInputField = InputFieldState("MMYY"),
        cvvInputFieldState = InputFieldState("CVV"),
        allowedPaymentMethodsIcons = emptyList(),
        allowedCardSchemes = emptyList()
    )
    val shippingAddress = ShippingAddressViewState(
        isVisible = true,
        currentSelectedCountry = SupportedCountriesViewEntity("", "ShippingCountryCode", true),
        supportedCountriesList = emptyList(),
        name = InputFieldState("ShippingName"),
        addressLine1 = InputFieldState("ShippingAddressLine1"),
        addressLine2 = InputFieldState("ShippingAddressLine2"),
        city = InputFieldState("ShippingCity"),
        postalCode = InputFieldState("ShippingPostalCode"),
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
        addressLine1 = InputFieldState("BillingAddressLine1"),
        addressLine2 = InputFieldState("BillingAddressLine2"),
        city = InputFieldState("BillingCity"),
        postalCode = InputFieldState("BillingPostalCode"),
    )
}
