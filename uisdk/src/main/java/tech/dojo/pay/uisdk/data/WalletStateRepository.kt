package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class WalletStateRepository {
    private val isWalletEnabled: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun updatePayment(isActive: Boolean) {
        isWalletEnabled.tryEmit(isActive)
    }

    fun observeWalletState() = isWalletEnabled.asStateFlow()
}
