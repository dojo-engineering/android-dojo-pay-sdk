package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.EssentialParamMissingException
import tech.dojo.pay.uisdk.domain.entities.ItemLinesDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

internal class PaymentIntentDomainEntityMapper {
    fun apply(raw: PaymentIntentPayload): PaymentIntentDomainEntity {
        checkInvalidParameters(raw)
        return PaymentIntentDomainEntity(
            id = requireNotNull(raw.id),
            customerId = raw.customer?.id,
            paymentToken = requireNotNull(raw.clientSessionSecret),
            amount = AmountDomainEntity(
                valueLong = requireNotNull(raw.amount?.value),
                valueString = requireNotNull(raw.amount?.value?.centsToString()),
                currencyCode = requireNotNull(raw.amount?.currencyCode)
            ),
            supportedCardsSchemes = requireNotNull(raw.merchantConfig?.supportedPaymentMethods?.cardSchemes?.mapNotNull { it }),
            supportedWalletSchemes = raw.merchantConfig?.supportedPaymentMethods?.wallets
                ?: emptyList(),
            itemLines = raw.itemLines?.map {
                ItemLinesDomainEntity(
                    amount = it.amountTotal,
                    caption = it.caption
                )
            },
            collectionEmailRequired = raw.config?.customerEmail?.collectionRequired ?: false,
            collectionBillingAddressRequired = raw.config?.billingAddress?.collectionRequired
                ?: false,
            isVirtualTerminalPayment = raw.paymentSource?.let { it.lowercase() == "virtual-terminal" } ?: false,
            isPreAuthPayment = raw.captureMode?.let { it.lowercase() == "manual" }?: false,
            orderId = raw.reference?: ""
        )
    }

    private fun checkInvalidParameters(raw: PaymentIntentPayload) {
        val invalidParams: MutableList<String> = mutableListOf()
        if (raw.id == null) invalidParams.add("id")
        if (raw.clientSessionSecret == null) invalidParams.add("clientSessionSecret")
        if (raw.amount == null) invalidParams.add("amount")
        if (raw.merchantConfig == null) invalidParams.add("merchantConfig")
        if (raw.merchantConfig?.supportedPaymentMethods == null) invalidParams.add("supportedPaymentMethods")
        if (raw.merchantConfig?.supportedPaymentMethods?.cardSchemes == null) invalidParams.add("cardSchemes")
        if (invalidParams.isNotEmpty()) throw EssentialParamMissingException(invalidParams)
    }
}
