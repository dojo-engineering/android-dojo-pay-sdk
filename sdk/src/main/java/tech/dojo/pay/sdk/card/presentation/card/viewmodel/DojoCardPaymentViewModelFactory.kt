package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.DeviceDataRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApiBuilder
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.presentation.threeds.CardinalConfigurator

internal class DojoCardPaymentViewModelFactory(
    private val arguments: Bundle?,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val params =
            args.getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoCardPaymentParams
        val api = CardPaymentApiBuilder().create()
        val cardPaymentRepository = CardPaymentRepository(api, params.token, params.paymentPayload)
        val dojo3DSRepository = Dojo3DSRepository(api, params.token)
        val deviceDataRepository = DeviceDataRepository(api, params.token)
        val cardinalConfigurator = CardinalConfigurator(context)
        val paymentPayload = params.paymentPayload
        val configuredCardinalInstance = cardinalConfigurator.getConfiguredCardinalInstance()
        return DojoCardPaymentViewModel(
            cardPaymentRepository,
            dojo3DSRepository,
            deviceDataRepository,
            paymentPayload,
            configuredCardinalInstance
        ) as T
    }
}
