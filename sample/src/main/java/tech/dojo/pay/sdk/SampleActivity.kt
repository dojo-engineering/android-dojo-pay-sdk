package tech.dojo.pay.sdk

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        findViewById<View>(R.id.btnCardPayment).setOnClickListener {
            startActivity(
                Intent(this, CardPaymentActivity::class.java)
            )
        }
    }
}