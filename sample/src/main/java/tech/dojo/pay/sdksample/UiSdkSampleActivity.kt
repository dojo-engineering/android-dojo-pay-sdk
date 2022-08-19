package tech.dojo.pay.sdksample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdksample.databinding.ActivityUiSdkSampleBinding
import tech.dojo.pay.sdksample.token.TokenGenerator
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
        uiSdkSampleBinding.startPaymentFlow.setOnClickListener {
            DojoSDKDropInUI.dojoThemeSettings = null
            dojoPayUI.startPaymentFlow(DojoPaymentFlowParams(uiSdkSampleBinding.token.text.toString()))
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
                    uiSdkSampleBinding.token.text.toString()
                )
            )
        }
        DojoSDKDropInUI.sandbox = uiSdkSampleBinding.checkboxSandbox.isChecked
    }

    private fun setTokenListener() {
        DojoSDKDropInUI.sandbox = uiSdkSampleBinding.checkboxSandbox.isChecked

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
                    displayToken(TokenGenerator.generateToken())
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

    private fun onSandboxChecked(isChecked: Boolean) {
        DojoSDKDropInUI.sandbox = isChecked
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
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
            .show()
    }
}
