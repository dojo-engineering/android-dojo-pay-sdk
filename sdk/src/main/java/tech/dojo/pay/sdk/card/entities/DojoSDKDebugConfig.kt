package tech.dojo.pay.sdk.card.entities

data class DojoSDKDebugConfig @JvmOverloads constructor(
    val urlConfig: DojoSDKURLConfig? = null,
    val isSandboxIntent: Boolean = false,
    val isSandboxWallet: Boolean = false
)

data class DojoSDKURLConfig @JvmOverloads constructor(
    val connectE: String? = null,
    val remote: String? = null
)
