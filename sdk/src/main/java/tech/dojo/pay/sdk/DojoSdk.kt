package tech.dojo.pay.sdk

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.card.DojoCardPaymentHandlerImpl
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

object DojoSdk {

    var sandbox: Boolean = false

    fun createCardPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoCardPaymentResult) -> Unit
    ): DojoCardPaymentHandler = DojoCardPaymentHandlerImpl(activity, onResult)
}