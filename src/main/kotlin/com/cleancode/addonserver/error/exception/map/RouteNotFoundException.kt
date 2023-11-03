package com.cleancode.addonserver.error.exception.map

import com.cleancode.addonserver.error.exception.BusinessException
import com.cleancode.addonserver.error.exception.ErrorCode

class RouteNotFoundException : BusinessException(ErrorCode.ROUTE_NOT_FOUND)
