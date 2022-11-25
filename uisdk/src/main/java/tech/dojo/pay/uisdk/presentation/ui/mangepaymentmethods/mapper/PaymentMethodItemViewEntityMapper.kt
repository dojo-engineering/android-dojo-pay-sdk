package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import java.util.Locale

internal class PaymentMethodItemViewEntityMapper {

    fun apply(
        FetchPaymentMethodsResult: FetchPaymentMethodsResult?,
        isWalletNEnabled: Boolean
    ): PaymentMethodItemViewEntity {
        val items = mutableListOf<PaymentMethodItemViewEntityItem>()
        if (isWalletNEnabled) {
            items.add(PaymentMethodItemViewEntityItem.WalletItemItem)
        }
        if (FetchPaymentMethodsResult is FetchPaymentMethodsResult.Success) {
            FetchPaymentMethodsResult.result.items.forEach { items.add(mapToCardItem(it)) }
        }
        return PaymentMethodItemViewEntity(items)
    }

    private fun mapToCardItem(it: PaymentMethodsDomainEntityItem) =
        PaymentMethodItemViewEntityItem.CardItemItem(
            id = it.id,
            scheme = it.scheme.cardsSchemes.replaceFirstChar { firstChar ->
                if (firstChar.isLowerCase()) firstChar.titlecase(
                    Locale.getDefault()
                ) else firstChar.toString()
            },
            pan = if (it.pan.length >8) {
                it.pan.substring(it.pan.length - 8, it.pan.length)
            } else {
                it.pan
            },
            icon = requireNotNull(getIcon(it)),
        )

    private fun getIcon(it: PaymentMethodsDomainEntityItem) =
        when (it.scheme) {
            CardsSchemes.VISA -> R.drawable.ic_visa
            CardsSchemes.MASTERCARD -> R.drawable.ic_mastercard
            CardsSchemes.MAESTRO -> R.drawable.ic_maestro
            CardsSchemes.AMEX -> R.drawable.ic_amex
            else -> null
        }
}
