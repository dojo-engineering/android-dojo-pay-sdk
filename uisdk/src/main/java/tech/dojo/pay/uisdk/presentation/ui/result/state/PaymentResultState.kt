package tech.dojo.pay.uisdk.presentation.ui.result.state

import androidx.annotation.DrawableRes

sealed class PaymentResultState {
    data class SuccessfulResult(
        @DrawableRes
        val imageId: Int,
        val status: String = "",
        val orderInfo: String = "",
        val description: String = "",
    ) : PaymentResultState()


    data class FailedResult(
        @DrawableRes
        val imageId: Int,
        val status: String = "",
        val details: String = ""
    ) : PaymentResultState()
}