package tech.dojo.pay.sdk.card.presentation.gpay

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.contract.TaskResultContracts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock

internal class TaskResultContractsTest {

    private val context: Context = mock()

    @Test
    fun `successful payment task returns payment data and success status`() {
        val paymentData = mock<PaymentData>()
        val task = Tasks.forResult(paymentData)

        val result = getPaymentDataResult().getSynchronousResult(context, task)?.value

        assertSame(paymentData, result?.result)
        assertTrue(result?.status?.isSuccess == true)
    }

    @Test
    fun `failed payment task preserves wallet error status`() {
        val status = Status(1234, "Wallet error")
        val task = Tasks.forException<PaymentData>(ApiException(status))

        val result = getPaymentDataResult().getSynchronousResult(context, task)?.value

        assertNull(result?.result)
        assertEquals(status, result?.status)
    }

    @Test
    fun `cancelled payment flow returns cancelled status`() {
        val result = getPaymentDataResult().parseResult(Activity.RESULT_CANCELED, null)

        assertNull(result.result)
        assertTrue(result.status.isCanceled)
    }

    private fun getPaymentDataResult(): TaskResultContracts.GetPaymentDataResult =
        TaskResultContracts.GetPaymentDataResult()
}
