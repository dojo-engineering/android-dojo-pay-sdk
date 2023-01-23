package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApiBuilder
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.presentation.threeds.CardinalConfigurator

internal class DojoGPayViewModelFactory(
    private val arguments: Bundle?,
    private val context: Context

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val params =
            args.getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoGPayParams
        val api = CardPaymentApiBuilder().create()
        val repo = GPayRepository(api, params.dojoPaymentIntent.token)
        val dojo3DSRepository = Dojo3DSRepository(api)
        val mapper = GpayPaymentRequestMapper(Gson())
        val cardinalConfigurator = CardinalConfigurator(context)
        val configuredCardinalInstance = cardinalConfigurator.getConfiguredCardinalInstance()

        return DojoGPayViewModel(repo, dojo3DSRepository, mapper, configuredCardinalInstance) as T
    }
}
