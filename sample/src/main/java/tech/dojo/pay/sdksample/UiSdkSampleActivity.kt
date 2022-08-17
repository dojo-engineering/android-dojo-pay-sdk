package tech.dojo.pay.sdksample

import android.graphics.Color
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
    private lateinit var binding: ActivityUiSdkSampleBinding
    private val dojoPayUI = DojoSDKDropInUI.createUIPaymentHandler(this) { result ->
        showResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUiSdkSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTokenListener()
        binding.startPaymentFlow.setOnClickListener {
            dojoPayUI.startPaymentFlow(DojoPaymentFlowParams(binding.token.text.toString()))
        }
        DojoSDKDropInUI.sandbox = binding.checkboxSandbox.isChecked
    }

    private fun setTokenListener() {
        DojoSDKDropInUI.sandbox = binding.checkboxSandbox.isChecked

        binding.checkboxSandbox.setOnCheckedChangeListener { _, isChecked ->
            binding.btnGenerateToken.visibility = if (isChecked) View.VISIBLE else View.GONE
            displayToken("")
            onSandboxChecked(isChecked)
        }

        binding.btnGenerateToken.setOnClickListener {
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
        binding.token.setText(token)
        binding.token.visibility = View.VISIBLE
    }

    private fun onSandboxChecked(isChecked: Boolean) {
        DojoSDKDropInUI.sandbox = isChecked
    }

    private fun showLoading() {
        binding.viewProgress.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.viewProgress.visibility = View.GONE
    }

    private fun showTokenError(e: Throwable) {
        binding.viewProgress.visibility = View.GONE
        binding.token.setText(e.message)
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
