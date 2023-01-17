package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.uisdk.data.entities.PaymentMethodsRaw
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem

internal class SupportedPaymentMethodsDomainMapper {

    fun apply(raw: PaymentMethodsRaw): PaymentMethodsDomainEntity {
        val items = mutableListOf<PaymentMethodsDomainEntityItem>()
        raw.savedPaymentMethods.forEach {
            items.add(
                PaymentMethodsDomainEntityItem(
                    id = it.id,
                    pan = it.cardDetails.pan,
                    expiryDate = it.cardDetails.expiryDate,
                    scheme = it.cardDetails.scheme
                )
            )
        }
        return PaymentMethodsDomainEntity(items)
    }
}
