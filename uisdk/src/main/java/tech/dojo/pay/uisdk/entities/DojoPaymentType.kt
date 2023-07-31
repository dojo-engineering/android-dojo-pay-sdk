package tech.dojo.pay.uisdk.entities

import java.io.Serializable

enum class DojoPaymentType : Serializable {
    PAYMENT_CARD,
    CARD_ON_FILE,
    VIRTUAL_TERMINAL,
}
