package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator

import org.junit.Assert
import org.junit.Test

internal class CardCheckoutScreenValidatorTest {

    @Test
    fun `calling isEmailValid with valied mail should return true`() {
        // arrange
        val email = "abc@gmail.com"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isEmailValid(email)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isEmailValid with InValid mail should return true`() {
        // arrange
        val email = "abc@gmail"
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isEmailValid(email)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isEmailFieldValidWithInputFieldVisibility with false for isInputFieldVisible should return true`() {
        // arrange
        val expected = true
        // act
        val actual =
            CardCheckoutScreenValidator().isEmailFieldValidWithInputFieldVisibility("", false)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isEmailFieldValidWithInputFieldVisibility with true  for isInputFieldVisible  and valid email should return true`() {
        // arrange
        val email = "abc@gmail.com"
        val expected = true
        // act
        val actual =
            CardCheckoutScreenValidator().isEmailFieldValidWithInputFieldVisibility(email, true)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isPostalCodeFieldWithInputFieldVisibility with visible input field and non empty value should return true `() {
        // arrange
        val code = "code"
        val expected = true
        // act
        val actual =
            CardCheckoutScreenValidator().isPostalCodeFieldWithInputFieldVisibility(code, true)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isPostalCodeFieldWithInputFieldVisibility with visible input field and  empty value should return false`() {
        // arrange
        val code = ""
        val expected = false
        // act
        val actual =
            CardCheckoutScreenValidator().isPostalCodeFieldWithInputFieldVisibility(code, true)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isPostalCodeFieldWithInputFieldVisibility with invisible input field  should return true`() {
        // arrange
        val expected = true
        // act
        val actual =
            CardCheckoutScreenValidator().isPostalCodeFieldWithInputFieldVisibility("", false)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid with valid card number should return true`() {
        // arrange
        val cardNumber = "4456530000001096"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid with short card number should return false`() {
        // arrange
        val cardNumber = "55555555"
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid for amex should return true`() {
        // arrange
        val cardNumber = "379999999999994"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid for visa should return true`() {
        // arrange
        val cardNumber = "4111111111111111"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid for mastercard should return true`() {
        // arrange
        val cardNumber = "5555555555554444"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid with inValid card number should return false`() {
        // arrange
        val cardNumber = "1234123412341234"
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardNumberValid with empty  card number should return false`() {
        // arrange
        val cardNumber = ""
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isCardNumberValid(cardNumber)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardExpireDateValid with valid expire date  should return true`() {
        // arrange
        val expireDate = "1224"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isCardExpireDateValid(expireDate)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardExpireDateValid with invalid expire date  should return false`() {
        // arrange
        val expireDate = "1221"
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isCardExpireDateValid(expireDate)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCardExpireDateValid with only month should return false`() {
        // arrange
        val expireDate = "12"
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isCardExpireDateValid(expireDate)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCvvValid with valid cvv should return true`() {
        // arrange
        val cvv = "122"
        val expected = true
        // act
        val actual = CardCheckoutScreenValidator().isCvvValid(cvv)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `calling isCvvValid with invalid cvv should return false`() {
        // arrange
        val cvv = "12"
        val expected = false
        // act
        val actual = CardCheckoutScreenValidator().isCvvValid(cvv)
        // assert
        Assert.assertEquals(expected, actual)
    }
}
