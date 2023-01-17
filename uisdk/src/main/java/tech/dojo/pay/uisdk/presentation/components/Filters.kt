package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText

private val ameRegex = Regex("^3[47][0-9]{0,}\$")
private val visaRegex = Regex("^4[0-9]{0,}\$")
private val masterCardRegex = Regex("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]{0,}\$")
private val maestroCardRegex = Regex("^(5[06789]|6)[0-9]{0,}\$")
private val digitsPattern = Regex("^\\d+\$")

/**
 * This will match if the card number is Amex card or not
 */
internal fun isAmexCardScheme(cardNumber: String): Boolean {
    val trimmedCardNumber = cardNumber.replace(" ", "")
    return trimmedCardNumber.matches(ameRegex)
}

/**
 * This will match if the card number is Visa card or not
 */
internal fun isVisaCardScheme(cardNumber: String): Boolean {
    val trimmedCardNumber = cardNumber.replace(" ", "")
    return trimmedCardNumber.matches(visaRegex)
}

/**
 * This will match if the card number is MasterCard card or not
 */
internal fun isMasterCardScheme(cardNumber: String): Boolean {
    val trimmedCardNumber = cardNumber.replace(" ", "")
    return trimmedCardNumber.matches(masterCardRegex)
}

/**
 * This will match if the card number is MaestroCard card or not
 */
internal fun isMaestroCardScheme(cardNumber: String): Boolean {
    val trimmedCardNumber = cardNumber.replace(" ", "")
    return trimmedCardNumber.matches(maestroCardRegex)
}

/**
 * This will match if the input value is number of not
 */
internal fun isDigit(newValue: TextFieldValue): Boolean {
    return newValue.text.isEmpty() || newValue.text.matches(digitsPattern)
}

/**
 * This will format the card number to follow amex format
 */
internal fun formatAmex(text: AnnotatedString): TransformedText {
    //    original - 345678901234564
    //    transformed 3456 7890123 4564
    //    xxxx xxxxxx xxxxx
    val trimmed = if (text.text.length >= 15) text.text.substring(0..14) else text.text
    var out = ""

    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i == 3 || i == 9 && i != 14) out += " "
    }

    /**
     * The offset translator should ignore the hyphen characters, so conversion from
     *  original offset to transformed text works like
     *  - The 4th char of the original text is 5th char in the transformed text. (i.e original[4th] == transformed[5th]])
     *  - The 11th char of the original text is 13th char in the transformed text. (i.e original[11th] == transformed[13th])
     *  Similarly, the reverse conversion works like
     *  - The 5th char of the transformed text is 4th char in the original text. (i.e  transformed[5th] == original[4th] )
     *  - The 13th char of the transformed text is 11th char in the original text. (i.e transformed[13th] == original[11th])
     */
    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 9) return offset + 1
            if (offset <= 15) return offset + 2
            return 17
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 11) return offset - 1
            if (offset <= 17) return offset - 2
            return 15
        }
    }
    return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
}

internal fun formatNormalCard(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text

    val annotatedString = AnnotatedString.Builder().run {
        for (i in trimmed.indices) {
            append(trimmed[i])
            if (i % 4 == 3 && i != 15) {
                append(" ")
            }
        }
        pushStyle(SpanStyle(color = Color.LightGray))
        toAnnotatedString()
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 1
            if (offset <= 11) return offset + 2
            if (offset <= 16) return offset + 3
            return 19
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 1
            if (offset <= 14) return offset - 2
            if (offset <= 19) return offset - 3
            return 16
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}

internal fun dateFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 3) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset + 1
            return 5
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 2) return offset
            if (offset <= 5) return offset - 1
            if (offset <= 10) return offset - 2
            return 4
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}
