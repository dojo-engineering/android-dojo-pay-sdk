package tech.dojo.pay.sdk.card

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.data.CardPaymentApiBuilder
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams

internal class DojoCardPaymentViewModelFactory(
    private val arguments: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val params = args.getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoCardPaymentParams
        val api = CardPaymentApiBuilder(DojoSdk.sandbox).create()
        val repo = CardPaymentRepository(api, params.token, params.paymentPayload)
        return DojoCardPaymentViewModel(repo) as T
    }
}
