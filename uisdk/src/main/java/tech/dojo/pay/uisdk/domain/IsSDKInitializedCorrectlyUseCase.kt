package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.entities.DojoPaymentType

internal class IsSDKInitializedCorrectlyUseCase {
    fun isSDKInitiatedCorrectly(
        paymentIntent: PaymentIntentDomainEntity,
        paymentType: DojoPaymentType,
    ) = when (paymentType) {
        DojoPaymentType.VIRTUAL_TERMINAL -> paymentIntent.isVirtualTerminalPayment
        DojoPaymentType.SETUP_INTENT -> paymentIntent.isSetUpIntentPayment
        DojoPaymentType.PAYMENT_CARD -> !paymentIntent.isVirtualTerminalPayment && !paymentIntent.isSetUpIntentPayment
    }
}
