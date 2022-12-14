package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import java.util.Locale

internal class PaymentMethodItemViewEntityMapper(private val isDarkModeEnabled: Boolean) {
    fun apply(
        FetchPaymentMethodsResult: FetchPaymentMethodsResult?,
        isWalletNEnabled: Boolean
    ): PaymentMethodItemViewEntity {
        val items = mutableListOf<PaymentMethodItemViewEntityItem>()
        if (isWalletNEnabled) {
            items.add(PaymentMethodItemViewEntityItem.WalletItemItem)
        }
        if (FetchPaymentMethodsResult is FetchPaymentMethodsResult.Success) {
            FetchPaymentMethodsResult.result.items.forEach {
                items.add(
                    mapToCardItem(
                        it,
                        isDarkModeEnabled
                    )
                )
            }
        }
        return PaymentMethodItemViewEntity(items)
    }

    private fun mapToCardItem(it: PaymentMethodsDomainEntityItem, isDarkModeEnabled: Boolean) =
        PaymentMethodItemViewEntityItem.CardItemItem(
            id = it.id,
            scheme = it.scheme.cardsSchemes.replaceFirstChar { firstChar ->
                if (firstChar.isLowerCase()) firstChar.titlecase(
                    Locale.getDefault()
                ) else firstChar.toString()
            },
            pan = if (it.pan.length > 8) {
                it.pan.substring(it.pan.length - 8, it.pan.length)
            } else {
                it.pan
            },
            icon = requireNotNull(getIcon(it, isDarkModeEnabled)),
        )

    private fun getIcon(it: PaymentMethodsDomainEntityItem, isDarkModeEnabled: Boolean) =
        when (it.scheme) {
            CardsSchemes.VISA -> {
                if (isDarkModeEnabled) R.drawable.ic_visa_dark else R.drawable.ic_visa
            }
            CardsSchemes.MASTERCARD -> R.drawable.ic_mastercard
            CardsSchemes.MAESTRO -> R.drawable.ic_maestro
            CardsSchemes.AMEX -> {
                if (isDarkModeEnabled) R.drawable.ic_amex_dark else R.drawable.ic_amex
            }
            else -> null
        }
}
