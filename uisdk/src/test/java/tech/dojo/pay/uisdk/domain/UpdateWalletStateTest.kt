package tech.dojo.pay.uisdk.domain

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.WalletStateRepository

class UpdateWalletStateTest {

    private lateinit var walletStateRepository: WalletStateRepository
    private lateinit var updateWalletState: UpdateWalletState

    @Before
    fun setUp() {
        walletStateRepository = mockk()
        updateWalletState = UpdateWalletState(walletStateRepository)
    }

    @Test
    fun `updateWalletState with isAvailable = true`() {
        every { walletStateRepository.updatePayment(any()) } just Runs

        updateWalletState.updateWalletState(true)

        verify(exactly = 1) { walletStateRepository.updatePayment(true) }
    }

    @Test
    fun `updateWalletState with isAvailable = false`() {
        every { walletStateRepository.updatePayment(any()) } just Runs

        updateWalletState.updateWalletState(false)

        verify(exactly = 1) { walletStateRepository.updatePayment(false) }
    }
}
