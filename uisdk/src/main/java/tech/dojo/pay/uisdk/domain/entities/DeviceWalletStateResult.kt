package tech.dojo.pay.uisdk.domain.entities

internal sealed class DeviceWalletStateResult {
    object Enabled : DeviceWalletStateResult()
    object Disabled : DeviceWalletStateResult()
    object None : DeviceWalletStateResult()
}
