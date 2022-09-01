package tech.dojo.pay.uisdk.presentation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.data.PaymentIntentRepository
import tech.dojo.pay.uisdk.data.PaymentStateRepository
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class PaymentFlowViewModelFactory(private val arguments: Bundle?) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val paymentId =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentId
                ?: ""
        val fetchPaymentIntentUseCase = FetchPaymentIntentUseCase(paymentIntentRepository)
        val observePaymentIntent = ObservePaymentIntent(paymentIntentRepository)
        val updatePaymentStateUseCase = UpdatePaymentStateUseCase(paymentStatusRepository)
        val observePaymentStatus = ObservePaymentStatus(paymentStatusRepository)

        return PaymentFlowViewModel(
            paymentId,
            fetchPaymentIntentUseCase,
            observePaymentIntent,
            updatePaymentStateUseCase,
            observePaymentStatus
        ) as T
    }

    companion object {
        val paymentIntentRepository by lazy { PaymentIntentRepository() }
        val paymentStatusRepository by lazy { PaymentStateRepository() }
    }
}
