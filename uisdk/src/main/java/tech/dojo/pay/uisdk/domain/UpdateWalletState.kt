package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.WalletStateRepository

internal class UpdateWalletState(
    private val walletStateRepository: WalletStateRepository
) {
    fun updateWalletState(isAvailable: Boolean) {
        walletStateRepository.updatePayment(isAvailable)
    }
}