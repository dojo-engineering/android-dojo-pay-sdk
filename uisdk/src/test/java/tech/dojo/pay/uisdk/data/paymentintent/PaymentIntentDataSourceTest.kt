package tech.dojo.pay.uisdk.data.paymentintent

import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.sdk.DojoSdk

internal class PaymentIntentDataSourceTest {

    @Before
    fun setUp() {
        mockkObject(DojoSdk)
    }

    @Test
    fun `when calling fetchPaymentIntent , fetchPaymentIntent from DojoSdk should be called`() {
        val paymentId = "paymentId"
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {}
        val sut = PaymentIntentDataSource()

        sut.fetchPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
        verify {
            DojoSdk.fetchPaymentIntent(
                paymentId, onPaymentIntentSuccess, onPaymentIntentFailed
            )
        }
    }

    @Test
    fun `when calling refreshPaymentIntent , refreshPaymentIntent from DojoSdk should be called`() {
        val paymentId = "paymentId"
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {}
        val sut = PaymentIntentDataSource()

        sut.refreshPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
        verify {
            DojoSdk.refreshPaymentIntent(
                paymentId, onPaymentIntentSuccess, onPaymentIntentFailed
            )
        }
    }
}
