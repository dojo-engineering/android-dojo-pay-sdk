package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesDataSource
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesRepository
import tech.dojo.pay.uisdk.domain.GetRefreshedPaymentTokenFlow
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.MakeCardPaymentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.CardCheckOutFullCardPaymentPayloadMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator

class CardDetailsCheckoutViewModelFactory(
    private val dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val isDarkModeEnabled: Boolean,
    private val context: Context,
    private val isStartDestination: Boolean,
    private val arguments: Bundle?,
    private val navigateToCardResult: (dojoPaymentResult: DojoPaymentResult) -> Unit,
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
        val fullCardPaymentPayloadMapper = CardCheckOutFullCardPaymentPayloadMapper()
        val stringProvider = StringProvider(context)

        val paymentType =
            (
                arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as?
                    DojoPaymentFlowParams
                )?.paymentType ?: DojoPaymentType.PAYMENT_CARD

        val refreshPaymentIntentRepository = RefreshPaymentIntentRepository()

        val refreshPaymentIntentUseCase =
            RefreshPaymentIntentUseCase(
                refreshPaymentIntentRepository,
                paymentType,
            )

        val getRefreshedPaymentTokenFlow = GetRefreshedPaymentTokenFlow(repo = refreshPaymentIntentRepository)
        val makeCardPaymentUseCase = MakeCardPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )

        return CardDetailsCheckoutViewModel(
            observePaymentIntent,
            dojoCardPaymentHandler,
            observePaymentStatus,
            getSupportedCountriesUseCase,
            supportedCountriesViewEntityMapper,
            allowedPaymentMethodsViewEntityMapper,
            cardCheckoutScreenValidator,
            fullCardPaymentPayloadMapper,
            stringProvider,
            isStartDestination,
            makeCardPaymentUseCase,
            navigateToCardResult,
        ) as T
    }
}
