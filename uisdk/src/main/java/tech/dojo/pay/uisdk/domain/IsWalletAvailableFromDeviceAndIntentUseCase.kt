package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.sdk.card.entities.WalletSchemes
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult

internal class IsWalletAvailableFromDeviceAndIntentUseCase(
    private val observePaymentIntent: ObservePaymentIntent,
    private val observeDeviceWalletState: ObserveDeviceWalletState,
) {
    suspend fun isAvailable(): Boolean {
        val paymentIntentResult =
            observePaymentIntent
                .observePaymentIntent()
                .filter { it is PaymentIntentResult.Success || it is PaymentIntentResult.FetchFailure }
                .firstOrNull()

        val deviceWalletState =
            observeDeviceWalletState
                .observe()
                .filter { it != null }
                .firstOrNull()

        return deviceWalletState == true &&
            paymentIntentResult is PaymentIntentResult.Success && paymentIntentResult.result.supportedWalletSchemes.contains(
            WalletSchemes.GOOGLE_PAY,
        )
    }
}