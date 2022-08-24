package tech.dojo.pay.sdk.payemntintent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentIntentResult
import tech.dojo.pay.sdk.payemntintent.data.PaymentIntentRepository

internal class PaymentIntentProvider(
    private val paymentIntentRepository: PaymentIntentRepository
) {
    fun fetchPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (val result = paymentIntentRepository.getPaymentIntent(paymentId)) {
                    is DojoPaymentIntentResult.Success -> onPaymentIntentSuccess(result.paymentIntentJson)
                    is DojoPaymentIntentResult.Failed -> onPaymentIntentFailed()
                }
            } catch (throwable: Throwable) {
                onPaymentIntentFailed()
            }
        }
    }

}