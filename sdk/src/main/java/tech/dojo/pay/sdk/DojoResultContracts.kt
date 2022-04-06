package tech.dojo.pay.sdk

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class DojoResultContracts {

    class CardPayment : ActivityResultContract<Params, Result>() {

        override fun createIntent(context: Context, input: Params?): Intent {
            return Intent(context, DojoCardPaymentActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Result {
            return Result()
        }
    }

    //TODO GooglePayPayment
}