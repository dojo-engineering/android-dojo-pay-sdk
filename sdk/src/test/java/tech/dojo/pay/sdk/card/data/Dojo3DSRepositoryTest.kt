package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import tech.dojo.pay.sdk.card.entities.ThreeDSParams


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class Dojo3DSRepositoryTest {
    @Mock
    lateinit var api: CardPaymentApi

    private lateinit var repo: Dojo3DSRepository

    @Test
    fun `calling fetch3dsPage should call fetchSecurePage from api`() = runTest {
        // arrange
        val params = ThreeDSParams("url", "jwt", "md")
        // act
        repo = Dojo3DSRepository(api)
        repo.fetch3dsPage(params)
        // Assert
        verify(api).fetchSecurePage(params.stepUpUrl, params.jwt, params.md)
    }
}