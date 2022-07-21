package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class Dojo3DSRepository(private val api: CardPaymentApi) {
    suspend fun fetch3dsPage(params: ThreeDSParams): String {
        return api.fetchSecurePage(params.stepUpUrl, params.jwt, params.md)
    }
}
