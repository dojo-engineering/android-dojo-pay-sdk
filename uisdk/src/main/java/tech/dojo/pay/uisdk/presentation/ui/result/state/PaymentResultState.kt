package tech.dojo.pay.uisdk.presentation.ui.result.state

import androidx.annotation.DrawableRes

data class PaymentResultState(
    @DrawableRes
    val imageId: Int,
    val status: String = "",
    val orderInfo: String = "",
    val description: String = "",
)
