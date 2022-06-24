package tech.dojo.pay.sdk.card.presentation.card.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.presentation.card.viewmodel.DojoCardPaymentViewModel
import tech.dojo.pay.sdk.card.presentation.card.viewmodel.DojoCardPaymentViewModelFactory
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class DojoCardPaymentActivity : AppCompatActivity() {

    private val viewModel: DojoCardPaymentViewModel by viewModels {
        DojoCardPaymentViewModelFactory(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        observeDeviceData()
        observeResult()
    }

    private fun observeDeviceData() {
        viewModel.deviceData.observe(this) { deviceData ->
            supportFragmentManager.commit {
                add(R.id.container, DojoFingerPrintFragment.newInstance(deviceData))
            }
        }
    }

    private fun observeResult() {
        viewModel.paymentResult.observe(this) { result ->
            when (result) {
                is PaymentResult.Completed -> returnResult(result.value)
                is PaymentResult.ThreeDSRequired -> navigate3DS(result.params)
            }
        }
    }

    private fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoCardPaymentResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        finish()
        overridePendingTransition(0, R.anim.exit)
    }

    private fun navigate3DS(params: ThreeDSParams) {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.enter, 0)
            replace(R.id.container, Dojo3DSFragment.newInstance(params))
        }
    }

    override fun onBackPressed() {
        if (viewModel.canExit) super.onBackPressed()
    }
}
