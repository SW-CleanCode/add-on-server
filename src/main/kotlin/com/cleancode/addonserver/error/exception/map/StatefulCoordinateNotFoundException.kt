package com.cleancode.addonserver.error.exception.map

import com.cleancode.addonserver.error.exception.EntityNotFoundException
import com.cleancode.addonserver.error.exception.ErrorCode

class StatefulCoordinateNotFoundException : EntityNotFoundException(
    ErrorCode.STATEFUL_COORDINATE_NOT_FOUND
)
