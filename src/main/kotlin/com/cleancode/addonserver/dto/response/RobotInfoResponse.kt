package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.entity.Robot

data class RobotInfoResponse(
    val nowX: Int,
    val nowY: Int,
) {
    companion object {
        fun createRobotInfoResponse(
            robot: Robot,
        ): RobotInfoResponse {
            return RobotInfoResponse(
                robot.x,
                robot.y,
            )
        }
    }
}
