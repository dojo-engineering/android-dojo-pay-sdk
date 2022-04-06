package tech.dojo.pay.sdk.card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

internal class DojoCardPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)

        val payload = intent.extras!!.getSerializable(DojoCardPaymentResultContract.KEY_PAYLOAD) as DojoCardPaymentPayload
        val sandboxMode = intent.extras!!.getBoolean(DojoCardPaymentResultContract.KEY_SANDBOX_MODE)

        lifecycleScope.launch {
            delay(1000)
            setResult(RESULT_OK)
            finish()
        }
    }
}