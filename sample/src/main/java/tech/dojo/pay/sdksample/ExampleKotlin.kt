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

//        DojoSDKDropInUI.dojoThemeSettings?.customCardDetailsNavigationTitle = "Custom title"

//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenTitleFail = "Custom title fail"
//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenMainTextFail = "Custom title main"
//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenAdditionalTextFail = "Custom additional text"
//
//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenTitleSuccess = "Custom title success"
//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenMainTextSuccess = "Custom title main s"
//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenAdditionalTextSuccess = "Custom title main s"

//        DojoSDKDropInUI.dojoThemeSettings?.customResultScreenOrderIdText = "Custom order Id"

        val button = findViewById<Button>(R.id.startPayment)
        button.setOnClickListener {
            dojoHandler.startPaymentFlow(
                DojoPaymentFlowParams(
                    paymentId = "pi_sandbox_UddFbS3y50qsebyYukRU7g",
                    // add this if you supports saved card
                    clientSecret = "",
                    // add this if you supports google pay
                    GPayConfig = null
                )
            )
        }
    }
}
