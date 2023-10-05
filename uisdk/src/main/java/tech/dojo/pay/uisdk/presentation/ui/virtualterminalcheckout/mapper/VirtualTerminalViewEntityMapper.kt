package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper

import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.PayButtonViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.PaymentDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.FIRST_SECTION_OFF_SET_DP
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.SECOND_SECTION_WITH_BILLING_OFF_SET_DP
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.SECOND_SECTION_WITH_SHIPPING_OFF_SET_DP
import java.util.Currency

internal class VirtualTerminalViewEntityMapper(
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper,
    private val allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper,
) {

    fun apply(
        paymentIntentResult: PaymentIntentResult.Success,
        supportedCountriesDomainEntity: List<SupportedCountriesDomainEntity>,
    ) = VirtualTerminalViewState(
        isLoading = false,
        paymentDetailsSection = getPaymentDetailsSectionWithPaymentIntent(
            paymentIntentResult,
        ),
        shippingAddressSection = getShippingAddressSectionWithPaymentIntent(
            paymentIntentResult,
            supportedCountriesDomainEntity,
        ),
        billingAddressSection = getBillingAddressSectionWithPaymentIntent(
            paymentIntentResult,
            supportedCountriesDomainEntity,
        ),
        cardDetailsSection = getCardDetailsSectionWithPaymentIntent(paymentIntentResult),
        payButtonSection = PayButtonViewState(),
    )

    private fun getPaymentDetailsSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        PaymentDetailsViewState(
            totalAmount = paymentIntentResult.result.amount.valueString,
            amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
            merchantName = paymentIntentResult.result.merchantName,
            orderId = paymentIntentResult.result.orderId,
        )

    private fun getShippingAddressSectionWithPaymentIntent(
        paymentIntentResult: PaymentIntentResult.Success,
        supportedCountriesDomainEntity: List<SupportedCountriesDomainEntity>,
    ): ShippingAddressViewState {
        val countryList = getSupportedCountriesList(supportedCountriesDomainEntity)
        val currentSelectedCountry = if (countryList.isNotEmpty()) {
            countryList[0]
        } else {
            SupportedCountriesViewEntity("", "", true)
        }
        return ShippingAddressViewState(
            isVisible = paymentIntentResult.result.collectionShippingAddressRequired,
            itemPoissonOffset = if (paymentIntentResult.result.collectionShippingAddressRequired) {
                FIRST_SECTION_OFF_SET_DP
            } else {
                0
            },
            supportedCountriesList = countryList,
            currentSelectedCountry = currentSelectedCountry,
        )
    }

    private fun getBillingAddressSectionWithPaymentIntent(
        paymentIntentResult: PaymentIntentResult.Success,
        supportedCountriesDomainEntity: List<SupportedCountriesDomainEntity>,
    ): BillingAddressViewState {
        val isSectionVisible =
            if (paymentIntentResult.result.collectionShippingAddressRequired && paymentIntentResult.result.collectionBillingAddressRequired) {
                false
            } else {
                paymentIntentResult.result.collectionBillingAddressRequired
            }
        val countryList = getSupportedCountriesList(supportedCountriesDomainEntity)
        val currentSelectedCountry = if (countryList.isNotEmpty()) {
            countryList[0]
        } else {
            SupportedCountriesViewEntity("", "", true)
        }
        return BillingAddressViewState(
            isVisible = isSectionVisible,
            itemPoissonOffset = if (isSectionVisible) {
                FIRST_SECTION_OFF_SET_DP
            } else {
                0
            },
            supportedCountriesList = countryList,
            currentSelectedCountry = currentSelectedCountry,
        )
    }

    private fun getCardDetailsSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        CardDetailsViewState(
            isVisible = true,
            itemPoissonOffset = if (!paymentIntentResult.result.collectionShippingAddressRequired && !paymentIntentResult.result.collectionBillingAddressRequired) {
                FIRST_SECTION_OFF_SET_DP
            } else if (paymentIntentResult.result.collectionShippingAddressRequired) {
                SECOND_SECTION_WITH_SHIPPING_OFF_SET_DP
            } else {
                SECOND_SECTION_WITH_BILLING_OFF_SET_DP
            },
            emailInputField = InputFieldState(
                value = "",
                isVisible = paymentIntentResult.result.collectionEmailRequired,
            ),
            cardHolderInputField = InputFieldState(value = ""),
            cardNumberInputField = InputFieldState(value = ""),
            cardExpireDateInputField = InputFieldState(value = ""),
            cvvInputFieldState = InputFieldState(value = ""),
            allowedPaymentMethodsIcons = allowedPaymentMethodsViewEntityMapper.apply(
                paymentIntentResult.result.supportedCardsSchemes,
            ),
        )

    private fun getSupportedCountriesList(supportedCountriesDomainEntity: List<SupportedCountriesDomainEntity>): List<SupportedCountriesViewEntity> {
        return supportedCountriesDomainEntity.map { supportedCountriesViewEntityMapper.apply(it) }
    }
}
