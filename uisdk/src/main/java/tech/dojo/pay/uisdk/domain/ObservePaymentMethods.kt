package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository

internal class ObservePaymentMethods(
    private val repo: PaymentMethodsRepository
) {
    fun observe() = repo.observePaymentMethods()
}