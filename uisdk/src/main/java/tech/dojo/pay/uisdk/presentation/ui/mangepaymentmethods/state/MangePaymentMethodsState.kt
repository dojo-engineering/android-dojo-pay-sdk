package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state

import androidx.annotation.DrawableRes

internal data class MangePaymentMethodsState(
    val appBarIconType: AppBarIconType,
    val paymentMethodItems: PaymentMethodItemViewEntity,
    var currentSelectedMethod: PaymentMethodItemViewEntityItem?,
    val isUsePaymentMethodButtonEnabled: Boolean,
    val showDialog: Boolean,
    val isInEditMode: Boolean,
    val isDeleteItemInProgress: Boolean
)

internal data class PaymentMethodItemViewEntity(
    val items: List<PaymentMethodItemViewEntityItem>
)

internal sealed class PaymentMethodItemViewEntityItem {

    internal object WalletItemItem : PaymentMethodItemViewEntityItem()

    internal data class CardItemItem(
        @DrawableRes
        val icon: Int,
        val id: String,
        val scheme: String,
        val pan: String
    ) : PaymentMethodItemViewEntityItem()

    internal object NoItem : PaymentMethodItemViewEntityItem()
}

internal enum class AppBarIconType {
    CLOSE, DELETE
}
