package tech.dojo.pay.sdk.card

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoGPayParams

internal class DojoCardPaymentResultContract : ActivityResultContract<DojoCardPaymentParams, DojoPaymentResult>() {

    override fun createIntent(context: Context, input: DojoCardPaymentParams): Intent {
        val intent = Intent(context, DojoCardPaymentActivity::class.java)
        intent.putExtra(KEY_PARAMS, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DojoPaymentResult {
        return if (resultCode == RESULT_OK && intent != null) {
            intent.getSerializableExtra(KEY_RESULT) as DojoPaymentResult
        } else {
            DojoPaymentResult.DECLINED
        }
    }

    internal companion object {
        const val KEY_PARAMS = "PARAMS"
        const val KEY_RESULT = "RESULT"
    }
}

internal class DojoGPayResultContract : ActivityResultContract<DojoGPayParams, DojoPaymentResult>() {

    override fun createIntent(context: Context, input: DojoGPayParams): Intent {
        val intent = Intent(context, DojoGPayActivity::class.java)
        intent.putExtra(KEY_PARAMS, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DojoPaymentResult {
        return if (resultCode == RESULT_OK && intent != null) {
            intent.getSerializableExtra(KEY_RESULT) as DojoPaymentResult
        } else {
            DojoPaymentResult.DECLINED
        }
    }

    internal companion object {
        const val KEY_PARAMS = "PARAMS"
        const val KEY_RESULT = "RESULT"
    }
}
