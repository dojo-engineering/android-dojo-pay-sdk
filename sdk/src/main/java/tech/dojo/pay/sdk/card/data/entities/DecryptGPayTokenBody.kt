package tech.dojo.pay.sdk.card.data.entities

internal data class DecryptGPayTokenBody(
    val protocolVersion: String,
    val signature: String,
    val intermediateSigningKey: IntermediateSigningKey,
    val signedMessage: String

)

internal data class IntermediateSigningKey(
    val signedKey: String,
    val signatures: List<String>
)