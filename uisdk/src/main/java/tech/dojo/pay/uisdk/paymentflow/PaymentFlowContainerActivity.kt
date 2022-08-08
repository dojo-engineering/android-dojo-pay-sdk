package tech.dojo.pay.uisdk.paymentflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.uisdk.R

class PaymentFlowContainerActivity : AppCompatActivity() {
    val arguments: Bundle? by lazy { intent.extras }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }
}
