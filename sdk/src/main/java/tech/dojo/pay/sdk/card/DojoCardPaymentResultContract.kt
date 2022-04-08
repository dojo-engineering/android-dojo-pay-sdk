package tech.dojo.pay.sdk.card

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResultStatus

class DojoCardPaymentResultContract : ActivityResultContract<DojoCardPaymentParams, DojoCardPaymentResultStatus>() {

    override fun createIntent(context: Context, input: DojoCardPaymentParams): Intent {
        val intent = Intent(context, DojoCardPaymentActivity::class.java)
        intent.putExtra(KEY_PARAMS, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DojoCardPaymentResultStatus {
        return if (resultCode == RESULT_OK && intent != null) {
            intent.getSerializableExtra(KEY_RESULT) as DojoCardPaymentResultStatus
        } else {
            DojoCardPaymentResultStatus.CANCELLED
        }
    }

    internal companion object {
        const val KEY_PARAMS = "PARAMS"
        const val KEY_RESULT = "RESULT"
    }
}