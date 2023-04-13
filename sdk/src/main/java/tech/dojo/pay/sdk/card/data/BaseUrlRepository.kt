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
    private var baseUrlRaw = ""

    fun getBaseUrl(): String {
        return runBlocking {
            val deferred = CompletableDeferred<String>()
            val job = CoroutineScope(Dispatchers.IO).launch {
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
                if (response.isSuccessful) {
                    baseUrlRaw = response.body()?.baseUrlRaw ?: ""
                    baseUrlRaw
                } else { "" }
            }
        } catch (e: Exception) { "" }
    }
}
