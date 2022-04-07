package tech.dojo.pay.sdk.card

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams

internal class DojoCardPaymentViewModelFactory(
    private val arguments: Bundle?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val params = args.getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoCardPaymentParams
        return DojoCardPaymentViewModel(params) as T
    }

}