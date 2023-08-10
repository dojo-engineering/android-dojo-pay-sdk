package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoVirtualTerminalHandler
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesDataSource
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesRepository
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.FullCardPaymentPayloadMapper
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.VirtualTerminalViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator.VirtualTerminalValidator

internal class VirtualTerminalViewModelFactory(
    private val isDarkModeEnabled: Boolean,
    private val virtualTerminalHandler: DojoVirtualTerminalHandler,
    private val context: Context,
    private val arguments: Bundle?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val observePaymentStatus =
            ObservePaymentStatus(PaymentFlowViewModelFactory.paymentStatusRepository)
        val updatePaymentStateUseCase =
            UpdatePaymentStateUseCase(PaymentFlowViewModelFactory.paymentStatusRepository)
        val supportedCountriesRepository = SupportedCountriesRepository(
            dataSource = SupportedCountriesDataSource(context),
        )
        val getSupportedCountriesUseCase = GetSupportedCountriesUseCase(
            supportedCountriesRepository = supportedCountriesRepository,
        )
        val supportedCountriesViewEntityMapper =
            SupportedCountriesViewEntityMapper()
        val allowedPaymentMethodsViewEntityMapper =
            AllowedPaymentMethodsViewEntityMapper(isDarkModeEnabled)
        val cardCheckoutScreenValidator = CardCheckoutScreenValidator()
        val virtualTerminalValidator = VirtualTerminalValidator(cardCheckoutScreenValidator)
        val fullCardPaymentPayloadMapper = FullCardPaymentPayloadMapper()
        val virtualTerminalViewEntityMapper = VirtualTerminalViewEntityMapper(
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
        )

        val paymentType =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as?
                    DojoPaymentFlowParams)?.paymentType ?: DojoPaymentType.PAYMENT_CARD

        val refreshPaymentIntentRepository = RefreshPaymentIntentRepository()

        val refreshPaymentIntentUseCase =
            RefreshPaymentIntentUseCase(
                refreshPaymentIntentRepository,
                paymentType,
            )

        return VirtualTerminalViewModel(
            observePaymentIntent,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            virtualTerminalValidator,
            virtualTerminalHandler,
            fullCardPaymentPayloadMapper,
            virtualTerminalViewEntityMapper,
            refreshPaymentIntentUseCase,
        ) as T
    }
}
