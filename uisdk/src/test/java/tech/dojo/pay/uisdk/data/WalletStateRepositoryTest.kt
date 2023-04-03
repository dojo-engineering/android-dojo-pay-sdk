package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class WalletStateRepositoryTest {

    private lateinit var repository: WalletStateRepository

    @Before
    fun setup() {
        repository = WalletStateRepository()
    }


    @Test
    fun `observeWalletState emits correct values`() = runTest {
        val isActive = true
        val expectedValue = isActive

        repository.updatePayment(isActive)
        val stream = repository.observeWalletState()

        // assert
        val job = launch { stream.collectLatest { Assert.assertEquals(expectedValue, it) } }
        job.cancel()
    }
}

