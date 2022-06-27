package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoTotalAmountPayload(
    val value: Long,
    val currencyCode: String,
): Serializable