package tech.dojo.pay.sdk.card.data.remote.baseurl

import retrofit2.Response
import retrofit2.http.GET
import tech.dojo.pay.sdk.card.data.entities.BaseUrlRaw

internal interface BaseUrlApi {
    @GET("rag-manifest.json")
    suspend fun getBaseUrl(): Response<BaseUrlRaw>
}
