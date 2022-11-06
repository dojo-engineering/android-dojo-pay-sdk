package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.flow.MutableStateFlow

internal class WalletStateRepository {
    private lateinit var isWalletEnabled: MutableStateFlow<Boolean?>

    fun updatePayment(isActive: Boolean) {
        isWalletEnabled = MutableStateFlow(null)
        isWalletEnabled.tryEmit(isActive)
    }

    fun observeWalletState() = isWalletEnabled
}
