package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.DeviceWalletStateRepository

internal class ObserveDeviceWalletState(
    private val deviceWalletStateRepository: DeviceWalletStateRepository,
) {

    fun observe() = deviceWalletStateRepository.observeDeviceWalletState()
}
