package com.cleancode.addonserver.error.exception

open class ExternalApiFailedException : BusinessException {

    constructor(errorCode: ErrorCode) : super(errorCode)

    constructor(message: String, errorCode: ErrorCode) : super(message, errorCode)
}
