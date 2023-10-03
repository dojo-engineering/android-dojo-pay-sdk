package tech.dojo.pay.uisdk.entities

import androidx.annotation.Keep
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import java.io.Serializable
@Keep
data class DojoPaymentFlowParams @JvmOverloads constructor(
    val paymentId: String,
    val clientSecret: String? = null,
    val GPayConfig: DojoGPayConfig? = null,
    val paymentType: DojoPaymentType = DojoPaymentType.PAYMENT_CARD,
) : Serializable
