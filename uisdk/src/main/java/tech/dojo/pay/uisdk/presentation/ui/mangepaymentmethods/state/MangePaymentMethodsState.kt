package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state

import androidx.annotation.DrawableRes

internal data class MangePaymentMethodsState(
    @DrawableRes
    val appBarIcon: Int,
    val paymentMethodItems: PaymentMethodItemViewEntity,
    val isUsePaymentMethodButtonEnabled: Boolean
)

internal data class PaymentMethodItemViewEntity(
    val items: List<PaymentMethodItemViewEntityItem>
)

internal sealed class PaymentMethodItemViewEntityItem {

    object WalletItemItem : PaymentMethodItemViewEntityItem()

    data class CardItemItem(
        @DrawableRes
        val icon: Int,
        val id: String,
        val scheme: String,
        val pan: String
    ) : PaymentMethodItemViewEntityItem()
}
