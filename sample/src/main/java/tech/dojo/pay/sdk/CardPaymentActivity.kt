package tech.dojo.pay.sdk

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.databinding.ActivityCardPaymentBinding
import tech.dojo.pay.sdk.token.TokenGenerator

class CardPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardPaymentBinding

    private val cardPayment = registerForActivityResult(DojoCardPaymentResultContract()) { result ->
        binding.viewProgress.visibility = View.GONE
        showDialog(
            title = "Payment result",
            message = "${result.name} (${result.code})"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPay.setOnClickListener {
            pay(
                DojoCardDetails(
                    cardNumber = "4456530000001096",
                    cardName = "Card holder",
                    expiryDate = "12 / 24",
                    cv2 = "020"
                )
            )
        }
    }

    private fun pay(cardDetails: DojoCardDetails) {
        binding.viewProgress.visibility = View.VISIBLE
        lifecycleScope.launch {
            val token = try {
                TokenGenerator.generateToken()
            } catch (e: Exception) {
                binding.viewProgress.visibility = View.GONE
                showDialog(
                    title = "Can't generate token",
                    message = e.message ?: ""
                )
                return@launch
            }

            val params = DojoCardPaymentParams(
                token = token,
                paymentPayload = DojoCardPaymentPayload(cardDetails),
                sandboxMode = true
            )

            cardPayment.launch(params)
        }
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
            .show()
    }

}