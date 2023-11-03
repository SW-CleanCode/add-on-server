package com.cleancode.addonserver.error.exception.map

import com.cleancode.addonserver.error.exception.BusinessException
import com.cleancode.addonserver.error.exception.ErrorCode

class NoMoreImportantCoordinateException : BusinessException(
    ErrorCode.NO_MORE_IMPORTANT_COORDINATE
)
