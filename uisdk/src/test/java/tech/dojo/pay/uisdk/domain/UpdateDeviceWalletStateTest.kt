package tech.dojo.pay.uisdk.domain

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository

class UpdateDeviceWalletStateTest {

    private lateinit var deviceWalletStateRepository: DeviceWalletStateRepository
    private lateinit var updateDeviceWalletState: UpdateDeviceWalletState

    @Before
    fun setUp() {
        deviceWalletStateRepository = mockk()
        updateDeviceWalletState = UpdateDeviceWalletState(deviceWalletStateRepository)
    }

    @Test
    fun `updateWalletState with isAvailable = true`() {
        every { deviceWalletStateRepository.updateDeviceWalletState(any()) } just Runs

        updateDeviceWalletState.updateDeviceWalletState(true)

        verify(exactly = 1) { deviceWalletStateRepository.updateDeviceWalletState(true) }
    }

    @Test
    fun `updateWalletState with isAvailable = false`() {
        every { deviceWalletStateRepository.updateDeviceWalletState(any()) } just Runs

        updateDeviceWalletState.updateDeviceWalletState(false)

        verify(exactly = 1) { deviceWalletStateRepository.updateDeviceWalletState(false) }
    }
}
