package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoVirtualTerminalHandler
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesDataSource
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesRepository
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator

class CardDetailsCheckoutViewModelFactory(
    private val dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val isDarkModeEnabled: Boolean,
    private val virtualTerminalHandler: DojoVirtualTerminalHandler,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val observePaymentStatus =
            ObservePaymentStatus(PaymentFlowViewModelFactory.paymentStatusRepository)
        val updatePaymentStateUseCase =
            UpdatePaymentStateUseCase(PaymentFlowViewModelFactory.paymentStatusRepository)
        val supportedCountriesRepository = SupportedCountriesRepository(
            dataSource = SupportedCountriesDataSource(context)
        )
        val getSupportedCountriesUseCase = GetSupportedCountriesUseCase(
            supportedCountriesRepository = supportedCountriesRepository
        )
        val supportedCountriesViewEntityMapper =
            SupportedCountriesViewEntityMapper()
        val allowedPaymentMethodsViewEntityMapper =
            AllowedPaymentMethodsViewEntityMapper(isDarkModeEnabled)
        val cardCheckoutScreenValidator = CardCheckoutScreenValidator()

        return CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            updatePaymentStateUseCase,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            virtualTerminalHandler
        ) as T
    }
}
