package tech.dojo.pay.sdk.card

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

class DojoCardPaymentResultContract(
    private val sandboxMode: Boolean = false
) : ActivityResultContract<DojoCardPaymentPayload, DojoCardPaymentResult>() {

    override fun createIntent(context: Context, input: DojoCardPaymentPayload): Intent {
        val intent = Intent(context, DojoCardPaymentActivity::class.java)
        intent.putExtra(KEY_SANDBOX_MODE, sandboxMode)
        intent.putExtra(KEY_PAYLOAD, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DojoCardPaymentResult {
        return DojoCardPaymentResult()
    }

    companion object {
        const val KEY_SANDBOX_MODE = "SANDBOX_MODE"
        const val KEY_PAYLOAD = "PAYLOAD"
    }
}