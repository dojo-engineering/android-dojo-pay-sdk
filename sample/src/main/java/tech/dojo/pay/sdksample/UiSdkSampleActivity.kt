package tech.dojo.pay.sdksample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdksample.customer.CustomerGenerator
import tech.dojo.pay.sdksample.databinding.ActivityUiSdkSampleBinding
import tech.dojo.pay.sdksample.token.PaymentIDGenerator
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoThemeSettings

class UiSdkSampleActivity : AppCompatActivity() {
    private lateinit var uiSdkSampleBinding: ActivityUiSdkSampleBinding
    private val dojoPayUI = DojoSDKDropInUI.createUIPaymentHandler(this) { result ->
        showResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiSdkSampleBinding = ActivityUiSdkSampleBinding.inflate(layoutInflater)
        setContentView(uiSdkSampleBinding.root)
        setTokenListener()
        setCustomerCreationListener()
        uiSdkSampleBinding.startPaymentFlow.setOnClickListener {
            DojoSDKDropInUI.dojoThemeSettings = null
            dojoPayUI.startPaymentFlow(
                DojoPaymentFlowParams(
                    uiSdkSampleBinding.token.text.toString(),
                    GPayConfig = DojoGPayConfig(
                        merchantName = "Dojo Cafe (Paymentsense)",
                        merchantId = "BCR2DN6T57R5ZI34",
                        gatewayMerchantId = "119784244252745"
                    )
                ),

            )
        }
        uiSdkSampleBinding.startPaymentFlowWithTheme.setOnClickListener {
            DojoSDKDropInUI.dojoThemeSettings = DojoThemeSettings(
                "#036bfc",
                "#036bfc",
                "#036bfc",
                "#036bfc",
                "#036bfc"
            )
            dojoPayUI.startPaymentFlow(
                DojoPaymentFlowParams(
                    uiSdkSampleBinding.token.text.toString(),
                    GPayConfig = DojoGPayConfig(
                        merchantName = "Dojo Cafe (Paymentsense)",
                        merchantId = "BCR2DN6T57R5ZI34",
                        gatewayMerchantId = "119784244252745"
                    )
                )
            )
        }
        DojoSDKDropInUI.isWalletSandBox = uiSdkSampleBinding.checkboxSandbox.isChecked
    }

    private fun setTokenListener() {
        DojoSDKDropInUI.isWalletSandBox = uiSdkSampleBinding.checkboxSandbox.isChecked

        uiSdkSampleBinding.checkboxSandbox.setOnCheckedChangeListener { _, isChecked ->
            uiSdkSampleBinding.btnGenerateToken.visibility =
                if (isChecked) View.VISIBLE else View.GONE
            displayToken("")
            onSandboxChecked(isChecked)
        }

        uiSdkSampleBinding.btnGenerateToken.setOnClickListener {
            lifecycleScope.launch {
                showLoading()
                try {
                    displayToken(PaymentIDGenerator.generatePaymentId(uiSdkSampleBinding.userId.text.toString()).id)
                } catch (e: Throwable) {
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
                    displayCustomerID(CustomerGenerator.generateCustomerId().id)
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

    private fun displayCustomerID(id: String) {
        uiSdkSampleBinding.userId.setText(id)
    }

    private fun onSandboxChecked(isChecked: Boolean) {
        DojoSDKDropInUI.isWalletSandBox = isChecked
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

    fun showResult(result: DojoPaymentResult) {
        showDialog(
            title = "Payment result",
            message = "${result.name} (${result.code})"
        )
        displayToken("")
        displayCustomerID("")
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
            .show()
    }
}
