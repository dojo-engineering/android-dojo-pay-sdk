package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.data.CardPaymentApiBuilder
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.entities.DojoGPayParams

internal class DojoGPayViewModelFactory(
    private val arguments: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val params = args.getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoGPayParams
        val api = CardPaymentApiBuilder(DojoSdk.sandbox).create()
        val repo = GPayRepository(api, params.dojoPaymentIntent.token, params.dojoPaymentIntent)
//        val repo = CardPaymentRepository(api, params.token, params.paymentPayload)
        return DojoGPayViewModel(repo) as T
    }

}