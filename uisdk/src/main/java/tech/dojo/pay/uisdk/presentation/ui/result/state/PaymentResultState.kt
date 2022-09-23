package tech.dojo.pay.uisdk.presentation.ui.result.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class PaymentResultState(
    @StringRes
    open val appBarTitleId: Int
) {
    data class SuccessfulResult(
        @StringRes
        override val appBarTitleId: Int,
        @DrawableRes
        val imageId: Int,
        @StringRes
        val status: Int,
        val orderInfo: String,
        val description: String,
    ) : PaymentResultState(appBarTitleId)

    data class FailedResult(
        @StringRes
        override val appBarTitleId: Int,
        @DrawableRes
        val imageId: Int,
        val showTryAgain: Boolean = true,
        @StringRes
        val status: Int,
        val orderInfo: String,
        val isTryAgainLoading: Boolean,
        val shouldNavigateToPreviousScreen: Boolean,
        @StringRes
        val details: Int
    ) : PaymentResultState(appBarTitleId)
}
