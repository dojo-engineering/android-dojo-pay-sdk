package tech.dojo.pay.sdk.payemntintent

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentIntentResult
import tech.dojo.pay.sdk.payemntintent.data.PaymentIntentApiBuilder
import tech.dojo.pay.sdk.payemntintent.data.PaymentIntentRepository

@Suppress("SwallowedException")
internal class PaymentIntentProvider(
    private val paymentIntentRepository: PaymentIntentRepository = PaymentIntentRepository(
        PaymentIntentApiBuilder().create(),
    ),
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val repositoryScope: CoroutineScope = CoroutineScope(dispatcher)
    fun fetchPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit,
    ) {
        repositoryScope.launch {
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

    fun fetchSetUpIntent(
        paymentId: String,
        onSetUpIntentSuccess: (paymentIntentJson: String) -> Unit,
        onSetUpIntentFailed: () -> Unit,
    ) {
        repositoryScope.launch {
            try {
                when (val result = paymentIntentRepository.getSetUpIntent(paymentId)) {
                    is DojoPaymentIntentResult.Success -> onSetUpIntentSuccess(result.paymentIntentJson)
                    is DojoPaymentIntentResult.Failed -> onSetUpIntentFailed()
                }
            } catch (throwable: Throwable) {
                onSetUpIntentFailed()
            }
        }
    }

    fun refreshPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit,
    ) {
        repositoryScope.launch {
            try {
                when (val result = paymentIntentRepository.refreshPaymentIntent(paymentId)) {
                    is DojoPaymentIntentResult.Success -> onPaymentIntentSuccess(result.paymentIntentJson)
                    is DojoPaymentIntentResult.Failed -> onPaymentIntentFailed()
                }
            } catch (throwable: Throwable) {
                onPaymentIntentFailed()
            }
        }
    }
}
