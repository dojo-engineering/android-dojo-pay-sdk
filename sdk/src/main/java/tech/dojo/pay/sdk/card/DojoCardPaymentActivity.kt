package tech.dojo.pay.sdk.card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.R

internal class DojoCardPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)

        lifecycleScope.launch {
            delay(1000)
            setResult(RESULT_OK)
            finish()
        }
    }
}