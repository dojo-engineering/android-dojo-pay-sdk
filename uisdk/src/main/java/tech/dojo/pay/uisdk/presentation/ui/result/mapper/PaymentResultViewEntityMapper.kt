package tech.dojo.pay.uisdk.presentation.ui.result.mapper

import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.ui.CustomStringProvider
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import java.util.Locale

internal class PaymentResultViewEntityMapper(
    private val stringProvider: StringProvider,
    private val paymentType: DojoPaymentType,
    private val isDarkModeEnabled: Boolean,
    private val customStringProvider: CustomStringProvider
) {
    fun mapTpResultState(result: DojoPaymentResult) =
        if (result == DojoPaymentResult.SUCCESSFUL) {
            buildSuccessfulResult()
        } else {
            buildFailedResult()
        }

    fun mapToOrderIdField(orderId: String): String =
        customStringProvider.resultScreenOrderIdText ?: String.format(
            Locale.getDefault(),
            "%s %s",
            stringProvider.getString(R.string.dojo_ui_sdk_order_info),
            orderId,
        )

    private fun buildSuccessfulResult() = PaymentResultState.SuccessfulResult(
        appBarTitle = getSuccessfulAppBarTitle(),
        imageId = R.drawable.ic_success_circle,
        status = getSuccessfulStatusTitle(),
        orderInfo = "",
        description = getSuccessDetails(),
    )

    private fun buildFailedResult() = PaymentResultState.FailedResult(
        appBarTitle = getFailedAppBarTitle(),
        imageId = getErrorImage(),
        shouldNavigateToPreviousScreen = false,
        status = getFailedStatusTitle(),
        details = getFailedDetails(),
        orderInfo = getFailedOrderInfo(),
    )

    private fun getFailedDetails() =
        when (paymentType) {
            DojoPaymentType.SETUP_INTENT -> stringProvider.getString(R.string.dojo_ui_sdk_payment_result_main_title_setup_intent_fail)
            DojoPaymentType.PAYMENT_CARD, DojoPaymentType.VIRTUAL_TERMINAL ->
                customStringProvider.resultScreenAdditionalTextFail ?: stringProvider.getString(R.string.dojo_ui_sdk_payment_result_failed_description)
        }

    private fun getSuccessDetails() =
        when (paymentType) {
            DojoPaymentType.SETUP_INTENT -> ""
            DojoPaymentType.PAYMENT_CARD, DojoPaymentType.VIRTUAL_TERMINAL ->
                customStringProvider.resultScreenAdditionalTextSuccess.orEmpty()
        }

    private fun getSuccessfulStatusTitle(): String {
        return when (paymentType) {
            DojoPaymentType.SETUP_INTENT -> stringProvider.getString(R.string.dojo_ui_sdk_payment_result_main_title_setup_intent_success)
            DojoPaymentType.PAYMENT_CARD, DojoPaymentType.VIRTUAL_TERMINAL ->
                customStringProvider.resultScreenMainTextSuccess ?: stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_success)
        }
    }

    private fun getFailedStatusTitle(): String {
        return when (paymentType) {
            DojoPaymentType.SETUP_INTENT -> stringProvider.getString(R.string.dojo_ui_sdk_payment_result_main_message_setup_intent_fail)
            DojoPaymentType.PAYMENT_CARD, DojoPaymentType.VIRTUAL_TERMINAL ->
                customStringProvider.resultScreenMainTextFail ?: stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_fail)
        }
    }

    private fun getSuccessfulAppBarTitle(): String {
        return when (paymentType) {
            DojoPaymentType.SETUP_INTENT -> stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_setup_intent_success)
            DojoPaymentType.PAYMENT_CARD, DojoPaymentType.VIRTUAL_TERMINAL ->
                customStringProvider.resultScreenTitleSuccess ?: stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_success)
        }
    }

    private fun getFailedAppBarTitle(): String {
        return when (paymentType) {
            DojoPaymentType.SETUP_INTENT -> stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_setup_intent_fail)
            DojoPaymentType.PAYMENT_CARD, DojoPaymentType.VIRTUAL_TERMINAL ->
                customStringProvider.resultScreenTitleFail ?: stringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_fail)
        }
    }

    private fun getFailedOrderInfo(): String? {
        return customStringProvider.resultScreenOrderIdText
    }

    private fun getErrorImage() = if (isDarkModeEnabled) {
        R.drawable.ic_error_dark
    } else {
        R.drawable.ic_error_circle
    }
}
