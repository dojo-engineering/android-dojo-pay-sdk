package tech.dojo.pay.uisdk.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository

class ObserveDeviceWalletStateTest {

    private val mockDeviceWalletStateRepository = mockk<DeviceWalletStateRepository>()
    private val observeDeviceWalletState = ObserveDeviceWalletState(mockDeviceWalletStateRepository)

    @Test
    fun `should observe wallet stream`() {
        // given
        val isWalletEnabled: MutableStateFlow<Boolean?> = MutableStateFlow(false)
        every { mockDeviceWalletStateRepository.observeDeviceWalletState() } returns isWalletEnabled

        // when
        observeDeviceWalletState.observe()

        // then
        verify { mockDeviceWalletStateRepository.observeDeviceWalletState() }
    }
}
