package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.EssentialParamMissingException
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

class PaymentIntentDomainEntityMapper {
    fun apply(raw: PaymentIntentPayload): PaymentIntentDomainEntity {
        checkInvalidParameters(raw)
        return PaymentIntentDomainEntity(
            paymentToken = requireNotNull(raw.clientSessionSecret),
            amount = AmountDomainEntity(
                value = raw.amount.value.centsToString(),
                currencyCode = raw.amount.currencyCode
            )
        )
    }

    private fun checkInvalidParameters(raw: PaymentIntentPayload) {
        val invalidParams: MutableList<String> = mutableListOf()
        if (raw.clientSessionSecret == null) invalidParams.add("clientSessionSecret")
        if (invalidParams.isNotEmpty()) throw EssentialParamMissingException(invalidParams)
    }
}
