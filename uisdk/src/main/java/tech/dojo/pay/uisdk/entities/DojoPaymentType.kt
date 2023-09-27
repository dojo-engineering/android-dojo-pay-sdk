package tech.dojo.pay.uisdk.entities

import androidx.annotation.Keep
import java.io.Serializable
@Keep
enum class DojoPaymentType : Serializable {
    PAYMENT_CARD,
    SETUP_INTENT,
    VIRTUAL_TERMINAL,
}
