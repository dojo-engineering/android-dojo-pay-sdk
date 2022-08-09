package tech.dojo.pay.uisdk.paymentflow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.paymentflow.contract.DojoPaymentFlowHandlerResultContract

class PaymentFlowContainerActivity : AppCompatActivity() {
    val arguments: Bundle? by lazy { intent.extras }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }

    fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoPaymentFlowHandlerResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        overridePendingTransition(0, tech.dojo.pay.sdk.R.anim.exit)
    }
}
