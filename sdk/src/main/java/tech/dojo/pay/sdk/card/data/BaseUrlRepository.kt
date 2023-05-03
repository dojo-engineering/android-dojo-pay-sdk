package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.dojo.pay.sdk.card.data.entities.BaseUrlResult
import tech.dojo.pay.sdk.card.data.remote.baseurl.BaseUrlApi
import tech.dojo.pay.sdk.card.data.remote.baseurl.BaseUrlApiBuilder
import java.text.SimpleDateFormat
import java.util.Locale

internal object BaseUrlRepository {
    internal var api: BaseUrlApi = BaseUrlApiBuilder.create()
    var dispatchers = Dispatchers.IO
    private var baseUrlRaw = ""

    fun getBaseUrl(): String {
        return baseUrlRaw.ifEmpty {
            val GCPResult = getBaseUrl(BASE_URL_GOOGLE_PROD)
            val AWSResult = getBaseUrl(BASE_URL_AWS_PROD)
            baseUrlRaw = if (GCPResult != null && AWSResult != null) { getBaseUrlWithDates(GCPResult, AWSResult) } else { GCPResult?.baseUrl ?: (AWSResult?.baseUrl ?: "") }
            baseUrlRaw
        }
    }

    private fun getBaseUrlWithDates(
        GCPResult: BaseUrlResult,
        AWSResult: BaseUrlResult
    ) = if (GCPResult.lastModifiedDate != null && AWSResult.lastModifiedDate != null) {
        if (isAWSNewerThanGCP(AWSResult.lastModifiedDate, GCPResult.lastModifiedDate)) {
            AWSResult.baseUrl
        } else {
            GCPResult.baseUrl
        }
    } else {
        ""
    }

    private fun getBaseUrl(url: String): BaseUrlResult? {
        return runBlocking {
            val deferred = CompletableDeferred<BaseUrlResult?>()
            val job = CoroutineScope(dispatchers).launch {
                val result = fetchBaseUrl(url)
                deferred.complete(result)
            }
            val result = deferred.await()
            job.join()
            result
        }
    }

    private suspend fun fetchBaseUrl(url: String): BaseUrlResult? {
        return try {
            val response = api.getBaseUrl(url)
            if (response.isSuccessful && response.body() != null) {
                val baseUrlRaw = "${response.body()?.baseUrl}/"
                val lastUpdated = response.headers()["last-modified"]
                return BaseUrlResult(baseUrlRaw, lastUpdated)
            } else { return null }
        } catch (e: Exception) { null }
    }

    private fun isAWSNewerThanGCP(dateStrAWS: String, dateStrGCP: String): Boolean {
        val dateFormat = SimpleDateFormat(LAST_UPDATE_DATE_FORMAT, Locale.US)
        val dateAWS = dateFormat.parse(dateStrAWS)
        val dateGCP = dateFormat.parse(dateStrGCP)
        return dateAWS.after(dateGCP)
    }
}

internal const val BASE_URL_GOOGLE_PROD =
    "https://storage.googleapis.com/rag-prod-manifest/rag-manifest.json"
internal const val BASE_URL_AWS_PROD = "https://d1vkrwwafyvizg.cloudfront.net/rag-manifest.json"

private const val LAST_UPDATE_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z"
