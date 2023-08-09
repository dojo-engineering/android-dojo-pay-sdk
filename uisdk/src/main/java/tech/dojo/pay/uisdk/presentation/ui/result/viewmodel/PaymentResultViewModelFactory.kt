package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class PaymentResultViewModelFactory(
    private val result: DojoPaymentResult,
    private val arguments: Bundle?,
    private val isDarkModeEnabled: Boolean,
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val paymentType =
            (
                arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as?
                    DojoPaymentFlowParams
                )?.paymentType
                ?: DojoPaymentType.PAYMENT_CARD

        val refreshPaymentIntentRepository = RefreshPaymentIntentRepository()

        val refreshPaymentIntent =
            RefreshPaymentIntentUseCase(
                refreshPaymentIntentRepository,
                paymentType,
            )
        return PaymentResultViewModel(
            result,
            observePaymentIntent,
            refreshPaymentIntent,
            isDarkModeEnabled,
        ) as T
    }
}
