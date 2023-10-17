package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository
import tech.dojo.pay.uisdk.domain.entities.DeviceWalletStateResult

class ObserveDeviceWalletStateTest {

    private val mockDeviceWalletStateRepository: DeviceWalletStateRepository = mock()
    private val observeDeviceWalletState = ObserveDeviceWalletState(mockDeviceWalletStateRepository)

    @Test
    fun `should observe wallet stream`() {
        // given
        given(observeDeviceWalletState.observe()).willReturn(
            MutableStateFlow(DeviceWalletStateResult.Disabled),
        )

        // when
        observeDeviceWalletState.observe()

        // then
        verify(mockDeviceWalletStateRepository).observeDeviceWalletState()
    }
}
