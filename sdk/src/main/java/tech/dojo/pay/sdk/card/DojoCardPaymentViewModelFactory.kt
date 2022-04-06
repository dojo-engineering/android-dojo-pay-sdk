package tech.dojo.pay.sdk.card

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

internal class DojoCardPaymentViewModelFactory(
    private val arguments: Bundle?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val args = requireNotNull(arguments)
        val payload = args.getSerializable(DojoCardPaymentResultContract.KEY_PAYLOAD) as DojoCardPaymentPayload
        val sandboxMode = args.getBoolean(DojoCardPaymentResultContract.KEY_SANDBOX_MODE)
        return DojoCardPaymentViewModel(payload) as T
    }

}