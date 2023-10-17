package tech.dojo.pay.uisdk.domain

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository

class UpdateDeviceWalletStateTest {

    private var deviceWalletStateRepository: DeviceWalletStateRepository = mock()
    private lateinit var updateDeviceWalletState: UpdateDeviceWalletState

    @Before
    fun setUp() {
        updateDeviceWalletState = UpdateDeviceWalletState(deviceWalletStateRepository)
    }

    @Test
    fun `when calling updateDeviceWalletState with isAvailable = true should call updateDeviceWalletState from deviceWalletStateRepository`() {
        doNothing().whenever(deviceWalletStateRepository).updateDeviceWalletState(true)

        updateDeviceWalletState.updateDeviceWalletState(true)

        verify(deviceWalletStateRepository).updateDeviceWalletState(true)
    }

    @Test
    fun `when calling updateDeviceWalletState with isAvailable = false should call updateDeviceWalletState from deviceWalletStateRepository`() {
        doNothing().whenever(deviceWalletStateRepository).updateDeviceWalletState(false)

        updateDeviceWalletState.updateDeviceWalletState(false)

        verify(deviceWalletStateRepository).updateDeviceWalletState(false)
    }
}
