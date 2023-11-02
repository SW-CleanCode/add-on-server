package com.cleancode.addonserver.error.exception.robot

import com.cleancode.addonserver.error.exception.ErrorCode
import com.cleancode.addonserver.error.exception.InvalidValueException

class InvalidRobotPositionException : InvalidValueException(
    ErrorCode.INVALID_ROBOT_POSITION,
)
