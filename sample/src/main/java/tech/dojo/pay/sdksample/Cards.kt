package tech.dojo.pay.sdksample

import tech.dojo.pay.sdk.card.entities.DojoCardDetails

object Cards {

    val ThreeDSV2 = DojoCardDetails(
        cardNumber = "5200000000001096",
        cardName = "Test Cardholder",
        expiryMonth = "12",
        expiryYear = "29",
        cv2 = "020"
    )

    val ThreeDSV1 = DojoCardDetails(
        cardNumber = "5200000000001070",
        cardName = "Test Cardholder",
        expiryMonth = "12",
        expiryYear = "29",
        cv2 = "020"
    )

    val NoThreeDS = DojoCardDetails(
        cardNumber = "5200000000001005",
        cardName = "Test Cardholder",
        expiryMonth = "12",
        expiryYear = "29",
        cv2 = "020"
    )

    val Decline = DojoCardDetails(
        cardNumber = "4456530000001013",
        cardName = "Test Cardholder",
        expiryMonth = "12",
        expiryYear = "29",
        cv2 = "341"
    )
}
