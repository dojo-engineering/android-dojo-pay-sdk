package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.domain.GetRefreshedPaymentTokenFlow
import tech.dojo.pay.uisdk.domain.MakeGpayPaymentUseCase
import tech.dojo.pay.uisdk.domain.MakeSavedCardPaymentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.paymentMethodsRepository
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.walletStateRepository
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.mapper.PaymentMethodCheckoutViewEntityMapper

class PaymentMethodCheckoutViewModelFactory(
    private val savedCardPaymentHandler: DojoSavedCardPaymentHandler,
    private val gpayPaymentHandler: DojoGPayHandler,
    private val arguments: Bundle?,
    private val navigateToCardResult: (dojoPaymentResult: DojoPaymentResult) -> Unit,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val paymentType =
            (
                arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as?
                    DojoPaymentFlowParams
                )?.paymentType ?: DojoPaymentType.PAYMENT_CARD
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val observePaymentMethods = ObservePaymentMethods(paymentMethodsRepository)
        val observePaymentStatus =
            ObservePaymentStatus(PaymentFlowViewModelFactory.paymentStatusRepository)
        val updatePaymentStateUseCase =
            UpdatePaymentStateUseCase(PaymentFlowViewModelFactory.paymentStatusRepository)
        val gPayConfig =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.GPayConfig

        val refreshPaymentIntentRepository = RefreshPaymentIntentRepository()
        val getRefreshedPaymentTokenFlow =
            GetRefreshedPaymentTokenFlow(repo = refreshPaymentIntentRepository)
        val refreshPaymentIntentUseCase =
            RefreshPaymentIntentUseCase(
                refreshPaymentIntentRepository,
                paymentType,
            )
        val observeWalletState = ObserveWalletState(walletStateRepository)

        val paymentMethodCheckoutViewEntityMapper = PaymentMethodCheckoutViewEntityMapper()
        val makeGpayPaymentUseCase = MakeGpayPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
        val makeSavedCardPaymentUseCase = MakeSavedCardPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
        return PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            gPayConfig,
            observePaymentStatus,
            observeWalletState,
            paymentMethodCheckoutViewEntityMapper,
            makeGpayPaymentUseCase,
            makeSavedCardPaymentUseCase,
            navigateToCardResult,
        ) as T
    }
}
