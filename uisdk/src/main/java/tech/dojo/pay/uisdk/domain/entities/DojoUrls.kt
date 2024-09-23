package tech.dojo.pay.uisdk.domain.entities

sealed class DojoUrls {

    companion object {
        private const val UK_TERMS_URL = "https://pay.dojo.tech/terms"
        private const val UK_PRIVACY_URL = "https://dojo.tech/legal/privacy/"

        private const val IE_TERMS_URL = "https://dojo.tech/ie/legal/website-terms-conditions/"
        private const val IE_PRIVACY_URL = "https://dojo.tech/ie/legal/privacy/"

        private const val ES_TERMS_URL = "https://dojo.tech/es/legal/terminos-y-condiciones/"
        private const val ES_PRIVACY_URL = "https://dojo.tech/es/legal/privacidad/"

        private const val IT_TERMS_URL = "https://dojo.tech/it/legal/termini-e-condizioni/"
        private const val IT_PRIVACY_URL = "https://dojo.tech/it/legal/privacy/"
    }

    abstract val privacy: String

    abstract val terms: String

    data class Uk(override val privacy: String = UK_PRIVACY_URL, override val terms: String = UK_TERMS_URL) : DojoUrls()

    data class Ie(override val privacy: String = IE_PRIVACY_URL, override val terms: String = IE_TERMS_URL) : DojoUrls()

    data class Es(override val privacy: String = ES_PRIVACY_URL, override val terms: String = ES_TERMS_URL) : DojoUrls()

    data class It(override val privacy: String = IT_PRIVACY_URL, override val terms: String = IT_TERMS_URL) : DojoUrls()
}
