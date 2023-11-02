package com.cleancode.addonserver.error.exception.robot

import com.cleancode.addonserver.error.exception.EntityNotFoundException
import com.cleancode.addonserver.error.exception.ErrorCode

class RobotNotFoundException : EntityNotFoundException(ErrorCode.ROBOT_NOT_FOUND)
