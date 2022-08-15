package tech.dojo.pay.uisdk.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class PaymentFlowContainerActivity : AppCompatActivity() {
    val arguments: Bundle? by lazy { intent.extras }
    private val FragmentManager.currentNavigationFragment: Fragment?
        get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }

    fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoPaymentFlowHandlerResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        overridePendingTransition(0, tech.dojo.pay.sdk.R.anim.exit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val currentFragment = supportFragmentManager.currentNavigationFragment
        currentFragment?.onActivityResult(requestCode, resultCode, data)
    }
}
