package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.EssentialParamMissingException
import tech.dojo.pay.uisdk.domain.entities.ItemLinesDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

class PaymentIntentDomainEntityMapper {
    fun apply(raw: PaymentIntentPayload): PaymentIntentDomainEntity {
        checkInvalidParameters(raw)
        return PaymentIntentDomainEntity(
            id = requireNotNull(raw.id),
            paymentToken = requireNotNull(raw.clientSessionSecret),
            amount = AmountDomainEntity(
                valueLong = requireNotNull(raw.amount?.value),
                valueString = requireNotNull(raw.amount?.value?.centsToString()),
                currencyCode = requireNotNull(raw.amount?.currencyCode)
            ),
            itemLines = raw.itemLines?.map {
                ItemLinesDomainEntity(
                    amount = it.amountTotal,
                    caption = it.caption
                )
            }
        )
    }

    private fun checkInvalidParameters(raw: PaymentIntentPayload) {
        val invalidParams: MutableList<String> = mutableListOf()
        if (raw.id == null) invalidParams.add("id")
        if (raw.clientSessionSecret == null) invalidParams.add("clientSessionSecret")
        if (raw.amount == null) invalidParams.add("amount")
        if (invalidParams.isNotEmpty()) throw EssentialParamMissingException(invalidParams)
    }
}
