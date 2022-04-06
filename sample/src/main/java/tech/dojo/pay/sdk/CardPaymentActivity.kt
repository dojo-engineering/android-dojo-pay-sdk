package tech.dojo.pay.sdk

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract

class CardPaymentActivity : AppCompatActivity() {

    private val cardPayment = registerForActivityResult(DojoCardPaymentResultContract()) {
        Toast.makeText(this, "Payment completed", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)

        findViewById<View>(R.id.btnPay).setOnClickListener {

        }
    }

}