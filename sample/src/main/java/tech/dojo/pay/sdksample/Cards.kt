package tech.dojo.pay.sdksample

import tech.dojo.pay.sdk.card.entities.DojoCardDetails

object Cards {

    val ThreeDSV2 = DojoCardDetails(
        cardNumber = "4456530000001096",
        cardName = "Card holder",
        expiryMonth = "12",
        expiryYear = "24",
        cv2 = "020"
    )

    val ThreeDSV1 = DojoCardDetails(
        cardNumber = "4456530000000007",
        cardName = "Card holder",
        expiryMonth = "12",
        expiryYear = "24",
        cv2 = "020"
    )

    val NoThreeDS = DojoCardDetails(
        cardNumber = "4456530000001013",
        cardName = "Card holder",
        expiryMonth = "12",
        expiryYear = "24",
        cv2 = "020"
    )
}
