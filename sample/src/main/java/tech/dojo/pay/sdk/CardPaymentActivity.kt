package tech.dojo.pay.sdk

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.databinding.ActivityCardPaymentBinding

class CardPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardPaymentBinding

    private val cardPayment = registerForActivityResult(DojoCardPaymentResultContract()) { result ->
        binding.viewProgress.visibility = View.GONE
        showToast(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPay.setOnClickListener {
            binding.viewProgress.visibility = View.VISIBLE
            cardPayment.launch(createParams())
        }
    }

    private fun createParams() = DojoCardPaymentParams(
        token = "0kyE4B2qIat_b2T7zAs3ddOS2xhM_xRLdWpzra8KMuKtHM3jZ3IsrVF0VnIq7euLBa-nypuju4a1FVAX5QZl0v8fI8CnNta1p3ooJGu4u_G8ca3C6bfe7260J3cKaOhLpqt4Zc6ydw2fGjWceVO6KY2Uoz8H",
        paymentPayload = createPayload(),
        sandboxMode = true
    )

    private fun createPayload() = DojoCardPaymentPayload(
        cardDetails = DojoCardDetails(
            cardNumber = "4456530000001096",
            cardName = "Card holder",
            expiryDate = "12 / 24",
            cv2 = "020"
        )
    )

    private fun showToast(result: DojoCardPaymentResult) {
        val message = when (result) {
            DojoCardPaymentResult.CANCELLED -> "Payment cancelled"
            else -> "Payment completed"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}