package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class DeviceWalletStateRepository {
    private val isWalletEnabledOnDevice: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun updateDeviceWalletState(isAvailable: Boolean) {
        isWalletEnabledOnDevice.tryEmit(isAvailable)
    }

    fun observeDeviceWalletState() = isWalletEnabledOnDevice.asStateFlow()
}
