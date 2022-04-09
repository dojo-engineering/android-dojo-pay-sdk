package tech.dojo.pay.sdk.card

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class DojoCardPaymentActivity : AppCompatActivity() {

    private val viewModel: DojoCardPaymentViewModel by viewModels {
        DojoCardPaymentViewModelFactory(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.paymentResult.observe(this) { result ->
            when (result) {
                is PaymentResult.Completed -> returnResult(result.value)
                is PaymentResult.ThreeDSRequired -> navigate3DS(result.params)
            }
        }
    }

    private fun returnResult(result: DojoCardPaymentResult) {
        val data = Intent()
        data.putExtra(DojoCardPaymentResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        finish()
    }

    private fun navigate3DS(params: ThreeDSParams) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, Dojo3DSFragment.newInstance(params))
            .commit()
    }

    override fun onBackPressed() {
        if (viewModel.canExit) super.onBackPressed()
    }
}