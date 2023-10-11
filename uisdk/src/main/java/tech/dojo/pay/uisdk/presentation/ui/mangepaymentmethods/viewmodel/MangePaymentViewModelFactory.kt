package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.domain.DeletePaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.IsWalletAvailableFromDeviceAndIntentUseCase
import tech.dojo.pay.uisdk.domain.ObserveDeviceWalletState
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.deviceWalletStateRepository
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.paymentMethodsRepository
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper.PaymentMethodItemViewEntityMapper

class MangePaymentViewModelFactory(
    private val customerId: String,
    private val arguments: Bundle?,
    private val isDarkModeEnabled: Boolean,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val customerSecret =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.clientSecret
                ?: ""
        val observeDeviceWalletState = ObserveDeviceWalletState(deviceWalletStateRepository)
        val observePaymentMethods =
            ObservePaymentMethods(paymentMethodsRepository)
        val mapper = PaymentMethodItemViewEntityMapper(isDarkModeEnabled)

        val deletePaymentMethodsUseCase = DeletePaymentMethodsUseCase(
            customerId,
            customerSecret,
            paymentMethodsRepository,
        )
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val isWalletAvailableFromDeviceAndIntentUseCase = IsWalletAvailableFromDeviceAndIntentUseCase(
            observePaymentIntent,
            observeDeviceWalletState,
        )
        return MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observePaymentMethods,
            mapper,
            isWalletAvailableFromDeviceAndIntentUseCase,
        ) as T
    }
}
