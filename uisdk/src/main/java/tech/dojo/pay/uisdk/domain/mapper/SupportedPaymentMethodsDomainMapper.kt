package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.uisdk.data.entities.PaymentMethodsRaw
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem

internal class SupportedPaymentMethodsDomainMapper {

    fun apply(raw: PaymentMethodsRaw): PaymentMethodsDomainEntity {
        val items = mutableListOf<PaymentMethodsDomainEntityItem>()
        val savedPaymentMethods = raw.savedPaymentMethods
        for (savedPaymentMethod in savedPaymentMethods) {
            items.add(
                PaymentMethodsDomainEntityItem(
                    id = savedPaymentMethod.id,
                    pan = savedPaymentMethod.cardDetails.pan,
                    expiryDate = savedPaymentMethod.cardDetails.expiryDate,
                    scheme = savedPaymentMethod.cardDetails.scheme
                )
            )
        }
        return PaymentMethodsDomainEntity(items)
    }
}
