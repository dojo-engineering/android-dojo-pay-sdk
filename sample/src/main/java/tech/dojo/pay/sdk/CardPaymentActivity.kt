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
        setCardDetails(Cards.ThreeDSV2)
        setCardListeners()

        binding.btnPay.setOnClickListener {
            pay(
                DojoCardDetails(
                    cardNumber = binding.cardNumber.text.toString(),
                    cardName = binding.cardHolder.text.toString(),
                    expiryDate = binding.expiryDate.text.toString(),
                    cv2 = binding.securityCode.text.toString()
                )
            )
        }
    }

    private fun pay(cardDetails: DojoCardDetails) {
        binding.viewProgress.visibility = View.VISIBLE
        lifecycleScope.launch {
            val token = try {
                TokenGenerator.generateToken()
            } catch (e: Throwable) {
                showTokenError(e)
                return@launch
            }

            displayToken(token)

            val params = DojoCardPaymentParams(
                token = token,
                paymentPayload = DojoCardPaymentPayload(cardDetails),
                sandboxMode = true
            )

            cardPayment.launch(params)
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

    private fun setCardDetails(details: DojoCardDetails) {
        binding.cardNumber.setText(details.cardNumber)
        binding.cardHolder.setText(details.cardName)
        binding.expiryDate.setText(details.expiryDate)
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