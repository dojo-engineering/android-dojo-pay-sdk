package tech.dojo.pay.uisdk.presentation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository
import tech.dojo.pay.uisdk.data.PaymentStateRepository
import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository
import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.FetchPaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.IsSDKInitializedCorrectlyUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.UpdateDeviceWalletState
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

internal class PaymentFlowViewModelFactory(private val arguments: Bundle?) :
    ViewModelProvider.Factory {

    companion object {
        val paymentIntentRepository by lazy { PaymentIntentRepository() }
        val paymentStatusRepository by lazy { PaymentStateRepository() }
        val paymentMethodsRepository by lazy { PaymentMethodsRepository() }
        val deviceWalletStateRepository by lazy { DeviceWalletStateRepository() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val paymentId =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentId
                ?: ""
        val customerSecret =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.clientSecret
                ?: ""
        val paymentType =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)
                ?.paymentType ?: DojoPaymentType.PAYMENT_CARD
        Log.d("PaymentFlowVMFactory", "create: Unpacked from Intent. paymentId='$paymentId', paymentType=$paymentType, clientSecret='${customerSecret.take(10)}...'")
        val fetchPaymentIntentUseCase = FetchPaymentIntentUseCase(paymentIntentRepository)
        val observePaymentIntent = ObservePaymentIntent(paymentIntentRepository)
        val updatePaymentStateUseCase = UpdatePaymentStateUseCase(paymentStatusRepository)
        val fetchPaymentMethodsUseCase =
            FetchPaymentMethodsUseCase(paymentMethodsRepository)
        val isSDKInitializedCorrectlyUseCase = IsSDKInitializedCorrectlyUseCase()
        val updateDeviceWalletState = UpdateDeviceWalletState(deviceWalletStateRepository)

        return PaymentFlowViewModel(
            paymentId = paymentId,
            customerSecret = customerSecret,
            paymentType = paymentType,
            fetchPaymentIntentUseCase = fetchPaymentIntentUseCase,
            observePaymentIntent = observePaymentIntent,
            fetchPaymentMethodsUseCase = fetchPaymentMethodsUseCase,
            updatePaymentStateUseCase = updatePaymentStateUseCase,
            isSDKInitializedCorrectlyUseCase = isSDKInitializedCorrectlyUseCase,
            updateDeviceWalletState = updateDeviceWalletState,
        ) as T
    }
}
