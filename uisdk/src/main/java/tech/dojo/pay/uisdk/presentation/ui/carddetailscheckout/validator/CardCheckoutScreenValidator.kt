package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator

import androidx.core.util.PatternsCompat
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.presentation.components.isAmexCardScheme
import tech.dojo.pay.uisdk.presentation.components.isMaestroCardScheme
import tech.dojo.pay.uisdk.presentation.components.isMasterCardScheme
import tech.dojo.pay.uisdk.presentation.components.isVisaCardScheme

@Suppress("NestedBlockDepth")
internal class CardCheckoutScreenValidator {
    fun isEmailValid(emailValue: String) =
        PatternsCompat.EMAIL_ADDRESS.matcher(emailValue).matches()

    fun isEmailFieldValidWithInputFieldVisibility(
        emailValue: String,
        isInputFieldVisible: Boolean,
    ): Boolean {
        return if (isInputFieldVisible) {
            isEmailValid(emailValue)
        } else {
            true
        }
    }

    fun isPostalCodeFieldWithInputFieldVisibility(
        postalCodeValue: String,
        isInputFieldVisible: Boolean,
    ): Boolean {
        return if (isInputFieldVisible) {
            postalCodeValue.isNotBlank()
        } else {
            true
        }
    }

    fun isCardNumberValid(cardNumberValue: String): Boolean {
        return if (cardNumberValue.isNotEmpty() && cardNumberValue.length > 14) {
            var sum = 0
            var alternate = false
            for (i in cardNumberValue.length - 1 downTo 0) {
                var n: Int = cardNumberValue.substring(i, i + 1).toInt()
                if (alternate) {
                    n *= 2
                    if (n > 9) {
                        n = n % 10 + 1
                    }
                }
                sum += n
                alternate = !alternate
            }
            sum % 10 == 0
        } else {
            false
        }
    }

    fun isCardSchemaSupported(cardNumberValue: String, availableCardSchemas: List<CardsSchemes>): Boolean {
        return availableCardSchemas.contains(getCardScheme(cardNumberValue))
    }

    fun getCardScheme(cardNumberValue: String): CardsSchemes? {
        if (isAmexCardScheme(cardNumberValue)) { return CardsSchemes.AMEX }
        if (isMaestroCardScheme(cardNumberValue)) { return CardsSchemes.MAESTRO }
        if (isMasterCardScheme(cardNumberValue)) { return CardsSchemes.MASTERCARD }
        if (isVisaCardScheme(cardNumberValue)) { return CardsSchemes.VISA }
        return null
    }

    fun isCardExpireDateValid(expireDate: String): Boolean {
        return if (expireDate.length == 4) {
            val month = expireDate.substring(0, 2).toInt()
            val year = expireDate.substring(2, 4).toInt()
            (1..12).contains(month) && (22..99).contains(year)
        } else {
            false
        }
    }

    fun isCvvValid(cvvValue: String): Boolean = cvvValue.length > 2

    fun isCheckBoxValid(isStartDestination: Boolean, isCheckBoxChecked: Boolean): Boolean {
        return if (isStartDestination) isCheckBoxChecked else true
    }
}
