package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.dojo.pay.sdk.card.data.remote.baseurl.BaseUrlApi
import tech.dojo.pay.sdk.card.data.remote.baseurl.BaseUrlApiBuilder

internal object BaseUrlRepository {
    private val api: BaseUrlApi = BaseUrlApiBuilder.create()
    var dispatchers= Dispatchers.IO
    private var baseUrlRaw = ""

    fun getBaseUrl(): String {
        return runBlocking {
            val deferred = CompletableDeferred<String>()
            val job = CoroutineScope(dispatchers).launch {
                val result = fetchBaseUrl()
                deferred.complete(result)
            }
            val result = deferred.await()
            job.join()
            result
        }
    }

    private suspend fun fetchBaseUrl(): String {
        return try {
            baseUrlRaw.ifEmpty {
                val response = api.getBaseUrl()
                if (response.isSuccessful && response.body() != null) {
                    baseUrlRaw = "${response.body()?.baseUrl}/"
                    baseUrlRaw
                } else { "" }
            }
        } catch (e: Exception) { "" }
    }
}
