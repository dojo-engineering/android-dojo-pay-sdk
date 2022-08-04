package tech.dojo.pay.uisdk.paymentflow.handler

import android.content.Intent
import androidx.activity.ComponentActivity
import tech.dojo.pay.uisdk.paymentflow.PaymentFlowContainerActivity


internal class DojoPaymentFlowHandlerImp(
    private val activity: ComponentActivity,
) : DojoPaymentFlowHandler {
    override fun startPaymentFlow() {
        activity.startActivity(Intent(activity, PaymentFlowContainerActivity::class.java))
    }
}