package tech.dojo.pay.uisdk.data.paymentmethods

import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.sdk.DojoSdk

internal class PaymentMethodsDataSourceTest {
    @Before
    fun setUp() {
        mockkObject(DojoSdk)
    }

    @Test
    fun `when calling fetchPaymentMethods , fetchPaymentMethods from DojoSdk should be called`() {
        val customerId = "customerId"
        val customerSecret = "customerSecret"
        val onFetchPaymentMethodsSuccess: (paymentMethodsJson: String) -> Unit = {}
        val onFetchPaymentMethodsFailed: () -> Unit = {}
        val sut = PaymentMethodsDataSource()

        sut.fetchPaymentMethods(customerId, customerSecret, onFetchPaymentMethodsSuccess, onFetchPaymentMethodsFailed)
        verify {
            DojoSdk.fetchPaymentMethods(
                customerId, customerSecret, onFetchPaymentMethodsSuccess, onFetchPaymentMethodsFailed
            )
        }
    }

    @Test
    fun `when calling deletePaymentMethods , deletePaymentMethods from DojoSdk should be called`() {
        val customerId = "customerId"
        val customerSecret = "customerSecret"
        val paymentMethodId = "paymentMethodId"
        val onDeletePaymentMethodsSuccess: () -> Unit = {}
        val onDeletePaymentMethodsFailed: () -> Unit = {}
        val sut = PaymentMethodsDataSource()

        sut.deletePaymentMethods(customerId, customerSecret, paymentMethodId, onDeletePaymentMethodsSuccess, onDeletePaymentMethodsFailed)
        verify {
            DojoSdk.deletePaymentMethods(
                customerId, customerSecret, paymentMethodId, onDeletePaymentMethodsSuccess, onDeletePaymentMethodsFailed
            )
        }
    }
}
