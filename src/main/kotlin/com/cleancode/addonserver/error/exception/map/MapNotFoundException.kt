package com.cleancode.addonserver.error.exception.map

import com.cleancode.addonserver.error.exception.EntityNotFoundException
import com.cleancode.addonserver.error.exception.ErrorCode

class MapNotFoundException : EntityNotFoundException(ErrorCode.MAP_NOT_FOUND)
