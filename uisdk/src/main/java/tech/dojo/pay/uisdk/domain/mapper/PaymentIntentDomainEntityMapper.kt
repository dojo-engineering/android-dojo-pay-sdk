package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.ItemLinesDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentStatusDomainEntity
import java.util.Currency

internal class PaymentIntentDomainEntityMapper {
    fun apply(raw: PaymentIntentPayload): PaymentIntentDomainEntity? {
        return if (checkInvalidParameters(raw)) {
            null
        } else {
            PaymentIntentDomainEntity(
                id = requireNotNull(raw.id),
                customerId = raw.customer?.id,
                paymentToken = requireNotNull(raw.clientSessionSecret),
                amount = AmountDomainEntity(
                    valueLong = requireNotNull(raw.amount?.value ?: raw.intendedAmount?.value),
                    valueString = requireNotNull(
                        raw.amount?.value?.centsToString()
                            ?: raw.intendedAmount?.value?.centsToString(),
                    ),
                    currencyCode = requireNotNull(
                        raw.amount?.currencyCode ?: raw.intendedAmount?.currencyCode,
                    ),
                ),
                supportedCardsSchemes = requireNotNull(raw.merchantConfig?.supportedPaymentMethods?.cardSchemes?.mapNotNull { it }),
                supportedWalletSchemes = raw.merchantConfig?.supportedPaymentMethods?.wallets
                    ?: emptyList(),
                itemLines = raw.itemLines?.map {
                    ItemLinesDomainEntity(
                        amount = it.amountTotal,
                        caption = it.caption,
                    )
                },
                collectionEmailRequired = raw.config?.customerEmail?.collectionRequired ?: false,
                collectionBillingAddressRequired = raw.config?.billingAddress?.collectionRequired
                    ?: false,
                collectionShippingAddressRequired = raw.config?.shippingDetails?.collectionRequired
                    ?: false,
                isVirtualTerminalPayment = raw.paymentSource?.let { it.lowercase() == "virtual-terminal" }
                    ?: false,
                isPreAuthPayment = raw.captureMode?.let { it.lowercase() == "manual" } ?: false,
                orderId = raw.reference ?: "",
                isSetUpIntentPayment = !raw.merchantInitiatedType.isNullOrBlank() && !raw.paymentSource.isNullOrBlank(),
                merchantName = raw.config?.tradingName ?: "",
                isPaymentAlreadyCollected =
                PaymentIntentStatusDomainEntity.fromStatus(requireNotNull(raw.status)) == PaymentIntentStatusDomainEntity.CAPTURED ||
                    PaymentIntentStatusDomainEntity.fromStatus(requireNotNull(raw.status)) == PaymentIntentStatusDomainEntity.AUTHORIZED,
            )
        }
    }

    private fun checkInvalidParameters(raw: PaymentIntentPayload): Boolean {
        val invalidParams: MutableList<String> = mutableListOf()
        if (raw.id == null) invalidParams.add("id")
        if (raw.clientSessionSecret == null) invalidParams.add("clientSessionSecret")
        if (raw.amount == null && raw.intendedAmount == null) invalidParams.add("amount")
        if (!isValidCurrencyCode(raw.amount?.currencyCode ?: raw.intendedAmount?.currencyCode)) invalidParams.add("currencyCode")
        if (raw.merchantConfig == null) invalidParams.add("merchantConfig")
        if (raw.merchantConfig?.supportedPaymentMethods == null) invalidParams.add("supportedPaymentMethods")
        if (raw.merchantConfig?.supportedPaymentMethods?.cardSchemes == null) invalidParams.add("cardSchemes")
        if (raw.status == null) invalidParams.add("status")
        return invalidParams.isNotEmpty()
    }

    private fun isValidCurrencyCode(currencyCode: String?): Boolean {
        return currencyCode?.let {
            try {
                Currency.getInstance(it)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        } ?: false
    }
}
