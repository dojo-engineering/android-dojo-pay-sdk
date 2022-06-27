package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoTotalAmountPayload(
    val valueInCents: Long,
    val currencyCode: String,
): Serializable