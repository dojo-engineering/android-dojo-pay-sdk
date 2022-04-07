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

class CardPaymentActivity : AppCompatActivity() {

    private val cardPayment = registerForActivityResult(DojoCardPaymentResultContract()) { result ->
        showToast(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)

        findViewById<View>(R.id.btnPay).setOnClickListener {
            cardPayment.launch(createParams())
        }
    }

    private fun createParams() = DojoCardPaymentParams(
        token = "token",
        sandboxMode = true,
        paymentPayload = createPayload()
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