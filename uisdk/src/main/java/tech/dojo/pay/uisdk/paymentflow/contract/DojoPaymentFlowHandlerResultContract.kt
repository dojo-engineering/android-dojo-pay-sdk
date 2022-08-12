package tech.dojo.pay.uisdk.paymentflow.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.paymentflow.PaymentFlowContainerActivity

internal class DojoPaymentFlowHandlerResultContract :
    ActivityResultContract<DojoPaymentFlowParams, DojoPaymentResult>() {

    override fun createIntent(context: Context, input: DojoPaymentFlowParams): Intent {
        val intent = Intent(context, PaymentFlowContainerActivity::class.java)
        intent.putExtra(KEY_PARAMS, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DojoPaymentResult {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
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
