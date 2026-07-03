package tech.dojo.pay.sdksample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdksample.customer.CustomerGenerator
import tech.dojo.pay.sdksample.databinding.ActivityUiSdkSampleBinding
import tech.dojo.pay.sdksample.token.PaymentIDGenerator
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.entities.DojoThemeSettings

class UiSdkSampleActivity : AppCompatActivity() {
    private lateinit var uiSdkSampleBinding: ActivityUiSdkSampleBinding
    private val dojoPayUI =
        DojoSDKDropInUI.createUIPaymentHandler(this) { result -> showResult(result) }

    companion object {
        private const val TAG = "UiSdkSampleActivity"
    }

    var secret = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiSdkSampleBinding = ActivityUiSdkSampleBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(uiSdkSampleBinding.root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
        setContentView(uiSdkSampleBinding.root)
        setTokenListener()
        setCustomerCreationListener()

        uiSdkSampleBinding.startPaymentFlow.setOnClickListener {
            DojoSDKDropInUI.dojoThemeSettings = DojoThemeSettings(forceLightMode = false)
            secret = if (secret.isEmpty()) uiSdkSampleBinding.clientSecret.text.toString() else ""
            print(secret)
            val paymentId = uiSdkSampleBinding.token.text.toString()
            Log.d(TAG, "startPaymentFlow clicked: paymentId='$paymentId', customerSecret='${secret.take(10)}...'")
            if (uiSdkSampleBinding.checkboxAdditonalLegalText.isChecked) {
                DojoSDKDropInUI.dojoThemeSettings?.additionalLegalText =
                    "Dojo is a trading name of Paymentsense Limited. Copyright ©2024 Paymentsense Limited. All rights reserved. Paymentsense Limited is authorised and regulated by the Financial Conduct Authority (FCA FRN 738728) and under the Electronic Money Regulations 2011 (FCA FRN 900925) for the issuing of electronic money and provision of payment services. Our company number is 06730690 and our registered office address is The Brunel Building, 2 Canalside Walk, London W2 1DG"
            }
            dojoPayUI.startPaymentFlow(
                DojoPaymentFlowParams(
                    uiSdkSampleBinding.token.text.toString(),
                    secret
                ),
            )
        }
        uiSdkSampleBinding.startPaymentFlowWithVT.setOnClickListener {
            DojoSDKDropInUI.dojoThemeSettings = DojoThemeSettings(forceLightMode = true)
            val paymentId = uiSdkSampleBinding.token.text.toString()
            Log.d(TAG, "startPaymentFlowWithVT clicked: paymentId='$paymentId', paymentType=VIRTUAL_TERMINAL")
            dojoPayUI.startPaymentFlow(
                DojoPaymentFlowParams(
                    uiSdkSampleBinding.token.text.toString(),
                    secret,
                    GPayConfig = DojoGPayConfig(
                        merchantName = "Dojo Cafe (Paymentsense)",
                        merchantId = "BCR2DN6T57R5ZI34",
                        gatewayMerchantId = "119784244252745",
                    ),
                    paymentType = DojoPaymentType.VIRTUAL_TERMINAL,
                ),
            )
        }
        uiSdkSampleBinding.startPaymentFlowCOF.setOnClickListener {
            DojoSDKDropInUI.dojoThemeSettings = DojoThemeSettings(forceLightMode = true)
            val paymentId = uiSdkSampleBinding.token.text.toString()
            Log.d(TAG, "startPaymentFlowCOF clicked: paymentId='$paymentId', paymentType=SETUP_INTENT")
            dojoPayUI.startPaymentFlow(
                DojoPaymentFlowParams(
                    uiSdkSampleBinding.token.text.toString(),
                    secret,
                    GPayConfig = DojoGPayConfig(
                        merchantName = "Dojo Cafe (Paymentsense)",
                        merchantId = "BCR2DN6T57R5ZI34",
                        gatewayMerchantId = "119784244252745",
                    ),
                    paymentType = DojoPaymentType.SETUP_INTENT,
                ),
            )
        }
    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val result = DojoSDKDropInUI.parseUIPaymentFlowResult(requestCode, resultCode, data)
//        if (result!= null ) showResult(result)
//    }

    private fun setTokenListener() {
        uiSdkSampleBinding.checkboxSandbox.setOnCheckedChangeListener { _, isChecked ->
            uiSdkSampleBinding.btnGenerateToken.visibility =
                if (isChecked) View.VISIBLE else View.GONE
            displayToken("")
        }

        uiSdkSampleBinding.btnGenerateToken.setOnClickListener {
            lifecycleScope.launch {
                showLoading()
                try {
                    val generatedId = PaymentIDGenerator.generatePaymentId(uiSdkSampleBinding.userId.text.toString()).id
                    Log.d(TAG, "btnGenerateToken: Generated paymentId='$generatedId'")
                    displayToken(generatedId)
                } catch (e: Throwable) {
                    Log.e(TAG, "btnGenerateToken: Failed to generate payment ID", e)
                    showTokenError(e)
                } finally {
                    hideLoading()
                }
            }
        }
    }

    private fun setCustomerCreationListener() {
        uiSdkSampleBinding.btnGenerateCustomerID.setOnClickListener {
            uiSdkSampleBinding.userId.setText("")
            lifecycleScope.launch {
                showLoading()
                try {
                    val id = CustomerGenerator.generateCustomerId().id
                    secret = CustomerGenerator.getCustomerSecrete(id).secret
                    displayCustomerSecrete(id)
                } catch (e: Throwable) {
                    showTokenError(e)
                } finally {
                    hideLoading()
                }
            }
        }
    }

    private fun displayToken(token: String) {
        uiSdkSampleBinding.token.setText(token)
        uiSdkSampleBinding.token.visibility = View.VISIBLE
    }

    private fun displayCustomerSecrete(id: String) {
        uiSdkSampleBinding.userId.setText(id)
    }

    private fun showLoading() {
        uiSdkSampleBinding.viewProgress.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        uiSdkSampleBinding.viewProgress.visibility = View.GONE
    }

    private fun showTokenError(e: Throwable) {
        uiSdkSampleBinding.viewProgress.visibility = View.GONE
        uiSdkSampleBinding.token.setText(e.message)
    }

    private fun showResult(result: DojoPaymentResult) {
        showDialog(
            title = "Payment result",
            message = "${result.name} (${result.code})",
        )
        displayToken("")
        displayCustomerSecrete("")
        secret = ""
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
            .show()
    }
}
