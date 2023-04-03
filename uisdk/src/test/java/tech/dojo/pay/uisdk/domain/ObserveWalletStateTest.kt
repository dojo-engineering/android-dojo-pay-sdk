package tech.dojo.pay.uisdk.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import tech.dojo.pay.uisdk.data.WalletStateRepository

class ObserveWalletStateTest {

    private val mockWalletStateRepository = mockk<WalletStateRepository>()
    private val observeWalletState = ObserveWalletState(mockWalletStateRepository)

    @Test
    fun `should observe wallet stream`() {
        // given
        val isWalletEnabled: MutableStateFlow<Boolean?> = MutableStateFlow(false)
        every { mockWalletStateRepository.observeWalletState() } returns isWalletEnabled

        // when
        observeWalletState.observe()

        // then
        verify { mockWalletStateRepository.observeWalletState() }
    }
}
