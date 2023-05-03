package tech.dojo.pay.sdk.card.data.remote.baseurl

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import tech.dojo.pay.sdk.card.data.entities.BaseUrlRaw

internal interface BaseUrlApi {
    @GET
    suspend fun getBaseUrl(
        @Url url: String
    ): Response<BaseUrlRaw>
}
