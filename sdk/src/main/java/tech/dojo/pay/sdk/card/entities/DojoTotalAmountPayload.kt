package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoTotalAmountPayload(
    val amount: Double,
    val currencyCode: String,
): Serializable