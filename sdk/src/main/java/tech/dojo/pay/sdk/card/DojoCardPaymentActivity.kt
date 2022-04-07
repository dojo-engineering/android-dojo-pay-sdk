package tech.dojo.pay.sdk.card

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.sdk.R

internal class DojoCardPaymentActivity : AppCompatActivity() {

    private val viewModel: DojoCardPaymentViewModel by viewModels {
        DojoCardPaymentViewModelFactory(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        observeResult()
    }

    private fun observeResult() {
        viewModel.result.observe(this) { result ->
            val data = Intent()
            data.putExtra(DojoCardPaymentResultContract.KEY_RESULT, result)
            setResult(RESULT_OK, data)
            finish()
        }
    }
}