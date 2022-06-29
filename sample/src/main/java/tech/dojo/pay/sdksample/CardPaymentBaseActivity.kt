package tech.dojo.pay.sdksample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoTotalAmountPayload
import tech.dojo.pay.sdk.card.presentation.gpay.util.DojoGPayEngine
import tech.dojo.pay.sdksample.databinding.ActivityCardPaymentBinding
import tech.dojo.pay.sdksample.token.TokenGenerator

abstract class CardPaymentBaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardPaymentBinding

    abstract fun onSandboxChecked(isChecked: Boolean)

    abstract fun onPayClicked(token: String, payload: DojoCardPaymentPayload)
    abstract fun onGPayClicked(token: String, payload: DojoTotalAmountPayload)

    fun showResult(result: DojoPaymentResult) {
        showDialog(
            title = "Payment result",
            message = "${result.name} (${result.code})"
        )
        displayToken("")
    }

    fun setProgressIndicatorVisible(visible: Boolean) {
        binding.viewProgress.isVisible = visible
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCardDetails(Cards.ThreeDSV2)
        setCardListeners()
        setTokenListener()

        binding.btnPay.setOnClickListener {
            val (month, year) = binding.expiryDate.text.toString().split("/")

            onPayClicked(
                token = binding.token.text.toString(),
                payload = DojoCardPaymentPayload(
                    DojoCardDetails(
                        cardNumber = binding.cardNumber.text.toString(),
                        cardName = binding.cardHolder.text.toString(),
                        expiryMonth = month,
                        expiryYear = year,
                        cv2 = binding.securityCode.text.toString()
                    )
                )
            )
        }
        DojoSdk.isGpayAvailable(this,
            { binding.btnGPay.googlePayButton.visibility = View.VISIBLE },
            { binding.btnGPay.googlePayButton.visibility = View.GONE }
        )
        binding.btnGPay.googlePayButton.setOnClickListener {
            onGPayClicked(
                token = binding.token.text.toString(),
                payload = DojoTotalAmountPayload(0.10, "GBP")
            )
        }
    }

    private fun setCardListeners() {
        binding.btn3DSV2.setOnClickListener {
            setCardDetails(Cards.ThreeDSV2)
        }

        binding.btn3DSV1.setOnClickListener {
            setCardDetails(Cards.ThreeDSV1)
        }

        binding.btnNo3DS.setOnClickListener {
            setCardDetails(Cards.NoThreeDS)
        }
    }

    private fun setTokenListener() {
        DojoSdk.sandbox = binding.checkboxSandbox.isChecked

        binding.checkboxSandbox.setOnCheckedChangeListener { _, isChecked ->
            binding.btnGenerateToken.visibility = if (isChecked) View.VISIBLE else View.GONE
            displayToken("")
            onSandboxChecked(isChecked)
        }

        binding.btnGenerateToken.setOnClickListener {
            binding.viewProgress.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    displayToken(TokenGenerator.generateToken())
                } catch (e: Throwable) {
                    showTokenError(e)
                } finally {
                    binding.viewProgress.visibility = View.GONE
                }
            }
        }
    }

    private fun setCardDetails(details: DojoCardDetails) {
        binding.cardNumber.setText(details.cardNumber)
        binding.cardHolder.setText(details.cardName)
        binding.expiryDate.setText("${details.expiryMonth}/${details.expiryYear}")
        binding.securityCode.setText(details.cv2)
    }

    private fun showTokenError(e: Throwable) {
        binding.viewProgress.visibility = View.GONE
        showDialog(
            title = "Can't generate token",
            message = e.message ?: ""
        )
    }

    private fun displayToken(token: String) {
        binding.token.setText(token)
        binding.token.visibility = View.VISIBLE
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
            .show()
    }
}
