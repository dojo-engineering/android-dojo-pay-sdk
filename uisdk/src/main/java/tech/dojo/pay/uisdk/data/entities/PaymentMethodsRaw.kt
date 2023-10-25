package tech.dojo.pay.uisdk.data.entities

import androidx.annotation.Keep
import tech.dojo.pay.sdk.card.entities.CardsSchemes
@Keep
internal data class PaymentMethodsRaw(
    val customerId: String,
    val merchantId: String,
    val savedPaymentMethods: List<SavedPaymentMethods>,
    val supportedPaymentMethods: SupportedPaymentMethods,
)

@Keep
internal data class SavedPaymentMethods(
    val id: String,
    val cardDetails: CardDetails,
)

@Keep
internal data class CardDetails(
    val pan: String,
    val expiryDate: String,
    val scheme: CardsSchemes,
)
