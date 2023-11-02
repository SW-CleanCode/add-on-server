package com.cleancode.addonserver.error

import com.cleancode.addonserver.error.exception.ErrorCode

data class ErrorResponse(
    val message: String,
    val status: Int,
    val code: String,
) {
    constructor(errorCode: ErrorCode) : this(
        message = errorCode.message,
        status = errorCode.status,
        code = errorCode.code,
    )
}
