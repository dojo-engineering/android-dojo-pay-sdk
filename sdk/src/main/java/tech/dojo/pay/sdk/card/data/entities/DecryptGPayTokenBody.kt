package tech.dojo.pay.sdk.card.data.entities

data class DecryptGPayTokenBody(
    val protocolVersion: String,
    val signature: String,
    val intermediateSigningKey: IntermediateSigningKey,
    val signedMessage: String

)

data class IntermediateSigningKey(
    val signedKey: String,
    val signatures: List<String>
)