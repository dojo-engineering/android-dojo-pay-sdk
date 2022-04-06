package tech.dojo.pay.sdk

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class CardPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)

        findViewById<View>(R.id.btnPay).setOnClickListener {
            startActivity(
                Intent(this, DojoCardPaymentActivity::class.java)
            )
        }
    }
}