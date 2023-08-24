package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.ItemLinesDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentStatusDomainEntity
import java.util.Currency

@Suppress("SwallowedException")
internal class PaymentIntentDomainEntityMapper {
    fun mapPayload(raw: PaymentIntentPayload): PaymentIntentDomainEntity? {
        return if (containsInvalidParameters(raw)) {
            null
        } else {
            mapToPaymentIntentDomainEntityWithValidRaw(raw)
        }
    }

    private fun containsInvalidParameters(raw: PaymentIntentPayload): Boolean {
        if (raw.id == null) return true
        if (raw.clientSessionSecret == null) return true
        if (raw.amount == null && raw.intendedAmount == null) return true
        if (!isValidCurrencyCode(
                raw.amount?.currencyCode ?: raw.intendedAmount?.currencyCode,
            )
        ) {
            return true
        }
        if (raw.merchantConfig == null) return true
        if (raw.merchantConfig.supportedPaymentMethods == null) return true
        if (raw.merchantConfig.supportedPaymentMethods.cardSchemes == null) return true
        if (raw.status == null) return true
        return false
    }

    private fun isValidCurrencyCode(currencyCode: String?): Boolean {
        return currencyCode?.let {
            try {
                Currency.getInstance(it) != null
            } catch (e: IllegalArgumentException) {
                false
            }
        } ?: false
    }

    private fun mapToPaymentIntentDomainEntityWithValidRaw(raw: PaymentIntentPayload) =
        PaymentIntentDomainEntity(
            id = raw.id.orEmpty(),
            customerId = raw.customer?.id,
            paymentToken = raw.clientSessionSecret.orEmpty(),
            amount = AmountDomainEntity(
                valueLong = raw.amount?.value ?: raw.intendedAmount?.value ?: 0L,
                valueString = (raw.amount?.value ?: raw.intendedAmount?.value)?.centsToString()
                    .orEmpty(),
                currencyCode = raw.amount?.currencyCode
                    ?: raw.intendedAmount?.currencyCode.orEmpty(),
            ),
            supportedCardsSchemes = raw.merchantConfig?.supportedPaymentMethods?.cardSchemes?.mapNotNull { it }
                ?: emptyList(),
            supportedWalletSchemes = raw.merchantConfig?.supportedPaymentMethods?.wallets.orEmpty(),
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
            isVirtualTerminalPayment = raw.paymentSource?.lowercase() == "virtual-terminal",
            isPreAuthPayment = raw.captureMode?.lowercase() == "manual",
            orderId = raw.reference.orEmpty(),
            isSetUpIntentPayment = !raw.merchantInitiatedType.isNullOrBlank() && !raw.paymentSource.isNullOrBlank(),
            merchantName = raw.config?.tradingName.orEmpty(),
            isPaymentAlreadyCollected =
            PaymentIntentStatusDomainEntity.fromStatus(raw.status.orEmpty())
                .let { it == PaymentIntentStatusDomainEntity.CAPTURED || it == PaymentIntentStatusDomainEntity.AUTHORIZED },
        )
}
