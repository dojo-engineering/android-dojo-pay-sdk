package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository

internal class UpdateDeviceWalletState(
    private val deviceWalletStateRepository: DeviceWalletStateRepository,
) {
    fun updateDeviceWalletState(isAvailable: Boolean) {
        deviceWalletStateRepository.updateDeviceWalletState(isAvailable)
    }
}
