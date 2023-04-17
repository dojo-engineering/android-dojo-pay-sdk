package tech.dojo.pay.sdk.card.entities

data class DojoSDKDebugConfig(
    val dojoSDKURLConfig: DojoSDKURLConfig? = null,
    val isSandboxIntent: Boolean = false,
    val isSandboxWallet: Boolean = false
)

data class DojoSDKURLConfig(
    val connectE: String? = null,
    val remote: String? = null
)
