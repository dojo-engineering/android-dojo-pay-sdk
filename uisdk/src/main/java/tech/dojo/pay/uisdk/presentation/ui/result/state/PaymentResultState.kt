package tech.dojo.pay.uisdk.presentation.ui.result.state

internal sealed class PaymentResultState(open val appBarTitle: String) {
    internal data class SuccessfulResult(
        override val appBarTitle: String,
        val imageId: Int,
        val status: String,
        val orderInfo: String,
        val description: String,
    ) : PaymentResultState(appBarTitle)

    internal data class FailedResult(
        override val appBarTitle: String,
        val imageId: Int,
        val status: String,
        val details: String,
        val orderInfo: String? = null,
        val shouldNavigateToPreviousScreen: Boolean,
    ) : PaymentResultState(appBarTitle)
}
