package tech.dojo.pay.sdk

enum class DojoPaymentResult(val code: Int) {
    SUCCESSFUL(0),
    AUTHORIZING(3),
    REFERRED(4),
    DECLINED(5),
    DUPLICATE_TRANSACTION(20),
    FAILED(30),
    WAITING_PRE_EXECUTE(99),
    INVALID_REQUEST(400),
    ISSUE_WITH_ACCESS_TOKEN(401),
    NO_ACCESS_TOKEN_SUPPLIED(404),
    INTERNAL_SERVER_ERROR(500),
    SDK_INTERNAL_ERROR(7770),
    USER_CLOSED_WITHOUT_PAYING(7780);

    companion object {
        fun fromCode(code: Int): DojoPaymentResult =
            values().find { it.code == code } ?: SDK_INTERNAL_ERROR
    }
}
