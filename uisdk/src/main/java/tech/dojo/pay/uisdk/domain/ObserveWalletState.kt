package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.WalletStateRepository

class ObserveWalletState(
    private val walletStateRepository: WalletStateRepository
) {

    fun observe() = walletStateRepository.observeWalletState()
}