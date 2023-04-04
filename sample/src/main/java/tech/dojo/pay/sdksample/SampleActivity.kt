package tech.dojo.pay.sdksample

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

        findViewById<View>(R.id.btnUICardPayment).setOnClickListener {
            startActivity(
                Intent(this, UiSdkSampleActivity::class.java)
            )
        }
        findViewById<View>(R.id.sampleAppDemoJava).setOnClickListener {
            startActivity(
                Intent(this, ExampleJava::class.java)
            )
        }

        findViewById<View>(R.id.sampleAppDemoKoltin).setOnClickListener {
            startActivity(
                Intent(this, ExampleKoltin::class.java)
            )
        }
    }
}
