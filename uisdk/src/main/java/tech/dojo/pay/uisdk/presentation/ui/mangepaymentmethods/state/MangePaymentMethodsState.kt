package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state

import androidx.annotation.DrawableRes

internal data class MangePaymentMethodsState(
    val tolBarIcon: Int,
    val paymentMethodItems: List<PaymentMethodItemViewEntity>,
    val isUsePaymentMethodButtonEnabled: Boolean
)

internal sealed class PaymentMethodItemViewEntity {

    object WalletItem : PaymentMethodItemViewEntity()

    data class CardItem(
        @DrawableRes
        val icon: Int,
        val scheme: String,
        val pan: String
    ) : PaymentMethodItemViewEntity()
}