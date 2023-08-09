package tech.dojo.pay.uisdk.entities

import java.io.Serializable

enum class DojoPaymentType : Serializable {
    PAYMENT_CARD,
    SETUP_INTENT,
    VIRTUAL_TERMINAL,
}
