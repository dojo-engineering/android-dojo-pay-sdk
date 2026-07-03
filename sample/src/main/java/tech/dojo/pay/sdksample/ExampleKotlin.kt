package tech.dojo.pay.sdksample

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams

class ExampleKotlin : AppCompatActivity() {
    val dojoHandler = DojoSDKDropInUI.createUIPaymentHandler(this) {
        // handle result
        Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_ui_sdk)

        val button = findViewById<Button>(R.id.startPayment)
        button.setOnClickListener {
            dojoHandler.startPaymentFlow(
                DojoPaymentFlowParams(
                    // TODO: Required — replace with a valid payment intent ID generated from your backend
                    paymentId = "",
                    // TODO: Required — replace with the clientSessionSecret returned after refreshing the client session secret
                    clientSecret = "",
                    // add this if you supports google pay
                    GPayConfig = null
                )
            )
        }
    }
}
