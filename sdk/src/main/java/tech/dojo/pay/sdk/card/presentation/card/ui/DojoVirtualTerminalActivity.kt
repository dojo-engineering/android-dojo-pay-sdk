package tech.dojo.pay.sdk.card.presentation.card.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.DojoCardVirtualTerminalResultContract
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.card.viewmodel.DojoVirtualTerminalViewModel
import tech.dojo.pay.sdk.card.presentation.card.viewmodel.DojoVirtualTerminalViewModelFactory

internal class DojoVirtualTerminalActivity : AppCompatActivity() {

    private val viewModel: DojoVirtualTerminalViewModel by viewModels {
        DojoVirtualTerminalViewModelFactory(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        observeResult()
    }

    private fun observeResult() {
        viewModel.paymentResult.observe(this) { result ->
            when (result) {
                is PaymentResult.Completed -> returnResult(result.value)
                is PaymentResult.ThreeDSRequired -> returnResult(DojoPaymentResult.FAILED)
            }
        }
    }

    private fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoCardVirtualTerminalResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        finish()
        overridePendingTransition(0, R.anim.exit)
    }

    override fun onBackPressed() {
        if (viewModel.canExit) super.onBackPressed()
    }
}
