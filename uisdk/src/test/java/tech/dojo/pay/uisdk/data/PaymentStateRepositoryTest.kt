package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class PaymentStateRepositoryTest {

    private val repository = PaymentStateRepository()

    @Test
    fun `updatePayment should update isPaymentInProgress`() = runBlocking {
        // Given
        val isActive = true

        // When
        repository.updatePayment(isActive)

        // Then
        assertEquals(isActive, repository.observePaymentIntent().first())
    }

    @Test
    fun `updateGpayPayment should update isGpayPaymentInProgress`() = runBlocking {
        // Given
        val isActive = true

        // When
        repository.updateGpayPayment(isActive)

        // Then
        assertEquals(isActive, repository.observeGpayPaymentIntent().first())
    }
}
