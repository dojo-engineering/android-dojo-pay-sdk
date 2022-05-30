package tech.dojo.pay.sdk.card

interface DojoGPayHandler {

    fun executeGPay(token: String)
}