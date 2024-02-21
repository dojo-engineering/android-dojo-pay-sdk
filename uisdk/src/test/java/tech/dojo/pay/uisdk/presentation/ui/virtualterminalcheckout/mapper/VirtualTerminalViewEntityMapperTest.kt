package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestDataVirtualTerminalViewEntityMapper.countryDomainList
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestDataVirtualTerminalViewEntityMapper.currentSelectedCountry
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestDataVirtualTerminalViewEntityMapper.paymentIntentDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.TestDataVirtualTerminalViewEntityMapper.virtualTerminalViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.PayButtonViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.PaymentDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState
import org.mockito.Mockito.`when` as whenever

class VirtualTerminalViewEntityMapperTest {

    @Mock
    private lateinit var supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper

    @Mock
    private lateinit var allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper

    private lateinit var mapper: VirtualTerminalViewEntityMapper

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mapper = VirtualTerminalViewEntityMapper(supportedCountriesViewEntityMapper, allowedPaymentMethodsViewEntityMapper)
    }

    @Test
    fun `given PaymentIntentResult and SupportedCountriesDomainEntity then return VirtualTerminalViewState`() {
        // arrange
        // Mock data
        val paymentIntentResult = PaymentIntentResult.Success(paymentIntentDomainEntity)
        val supportedCountriesDomainEntity = countryDomainList
        val supportedCountriesViewEntity = currentSelectedCountry
        val expectedVirtualTerminalViewState = virtualTerminalViewState

        // Mock behavior of the mapper dependencies
        whenever(supportedCountriesViewEntityMapper.apply(any())).thenReturn(supportedCountriesViewEntity)
        whenever(allowedPaymentMethodsViewEntityMapper.apply(any())).thenReturn(emptyList())

        // act
        val result = mapper.apply(paymentIntentResult, supportedCountriesDomainEntity)

        // assert
        assertEquals(expectedVirtualTerminalViewState, result)
    }
}

private object TestDataVirtualTerminalViewEntityMapper {
    val paymentIntentDomainEntity = PaymentIntentDomainEntity(
        id = "id",
        paymentToken = "token",
        totalAmount = AmountDomainEntity(
            10L,
            "100",
            "GBP",
        ),
        supportedCardsSchemes = listOf(CardsSchemes.AMEX),
        collectionBillingAddressRequired = true,
        customerId = "customerId",
        orderId = "orderId",
    )

    val countryDomainList = listOf(
        SupportedCountriesDomainEntity("United States", "US", true),
        SupportedCountriesDomainEntity("Canada", "CA", true),
    )

    val currentSelectedCountry = SupportedCountriesViewEntity(countryName = "EGP", countryCode = "EG", isPostalCodeEnabled = true)
    val virtualTerminalViewState = VirtualTerminalViewState(
        isLoading = false,
        paymentDetailsSection = PaymentDetailsViewState(
            merchantName = "",
            totalAmount = "100",
            amountCurrency = "£",
            orderId = "orderId",
        ),
        shippingAddressSection = ShippingAddressViewState(
            isVisible = false,
            name = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            addressLine1 = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            addressLine2 = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            city = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            postalCode = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            supportedCountriesList = listOf(
                SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
            ),
            currentSelectedCountry = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            ),
            deliveryNotes = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            isShippingSameAsBillingCheckBox = CheckBoxItem(
                messageText = R.string.dojo_ui_sdk_card_details_checkout_billing_same_as_shipping,
                isChecked = true,
                isVisible = true,
            ),
        ),
        billingAddressSection = BillingAddressViewState(
            isVisible = true,
            addressLine1 = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            addressLine2 = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            city = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            postalCode = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            supportedCountriesList = listOf(
                SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
                SupportedCountriesViewEntity(
                    countryName = "EGP",
                    countryCode = "EG",
                    isPostalCodeEnabled = true,
                ),
            ),
            currentSelectedCountry = SupportedCountriesViewEntity(
                countryName = "EGP",
                countryCode = "EG",
                isPostalCodeEnabled = true,
            ),
        ),
        cardDetailsSection = CardDetailsViewState(
            isVisible = true,
            emailInputField = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = false),
            cardHolderInputField = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            cardNumberInputField = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            cardExpireDateInputField = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            cvvInputFieldState = InputFieldState(value = "", errorMessages = null, isError = false, isVisible = true),
            allowedPaymentMethodsIcons = emptyList(),
        ),
        payButtonSection = PayButtonViewState(
            isEnabled = false,
            isLoading = false,
        ),
    )
}
