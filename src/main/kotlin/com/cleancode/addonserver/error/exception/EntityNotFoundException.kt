package com.cleancode.addonserver.error.exception

open class EntityNotFoundException : BusinessException {

    constructor(errorCode: ErrorCode) : super(errorCode)

    constructor(message: String) : super(message, ErrorCode.ENTITY_NOT_FOUND)
}
