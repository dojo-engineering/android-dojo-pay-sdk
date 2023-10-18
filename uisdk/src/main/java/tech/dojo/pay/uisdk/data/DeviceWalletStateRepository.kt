package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.dojo.pay.uisdk.domain.entities.DeviceWalletStateResult

internal class DeviceWalletStateRepository {
    private val isWalletEnabledOnDevice: MutableStateFlow<DeviceWalletStateResult> =
        MutableStateFlow(DeviceWalletStateResult.None)

    fun updateDeviceWalletState(isAvailable: Boolean) {
        if (isAvailable) {
            isWalletEnabledOnDevice.tryEmit(DeviceWalletStateResult.Enabled)
        } else {
            isWalletEnabledOnDevice.tryEmit(DeviceWalletStateResult.Disabled)
        }
    }

    fun observeDeviceWalletState() = isWalletEnabledOnDevice.asStateFlow()
}
