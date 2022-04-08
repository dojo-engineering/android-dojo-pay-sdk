package tech.dojo.pay.sdk.card.data

class CardPaymentRepository(private val api: CardPaymentApi) {

    suspend fun collectDeviceData() {
        api.collectDeviceData()
    }
}

