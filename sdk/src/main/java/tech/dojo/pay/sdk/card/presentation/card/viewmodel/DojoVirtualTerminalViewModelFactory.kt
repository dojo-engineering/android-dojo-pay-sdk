package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApiBuilder
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams

internal class DojoVirtualTerminalViewModelFactory(private val arguments: Bundle?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val params =
            args.getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoCardPaymentParams
        val api = CardPaymentApiBuilder().create()
        val cardPaymentRepository = CardPaymentRepository(api, params.token, params.paymentPayload)
        return DojoVirtualTerminalViewModel(cardPaymentRepository) as T
    }
}
