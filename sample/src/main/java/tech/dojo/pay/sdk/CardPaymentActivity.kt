package tech.dojo.pay.sdk

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails
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
        token = "token",
        paymentPayload = createPayload(),
        sandboxMode = true
    )

    private fun createPayload() = DojoCardPaymentPayload(
        cardDetails = DojoCardDetails(
            cardNumber = "1234123412341234",
            cardName = "Monzo",
            expiryDate = "12 / 22",
            cv2 = "123"
        ),
        userEmailAddress = "client@gmail.com",
        userPhoneNumber = "+4412345678",
        billingAddress = DojoAddressDetails(
            address1 = "Address 1",
            address2 = "Address 2",
            address3 = "Address 3",
            address4 = "Address 4",
            city = "London",
            state = "California",
            postcode = "N126YT",
            countryCode = "GBP"
        ),
        shippingDetails = DojoShippingDetails(
            name = "My home",
            address = DojoAddressDetails()
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