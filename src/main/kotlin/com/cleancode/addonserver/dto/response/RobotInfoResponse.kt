package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.entity.StatefulCoordinate

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

        fun createRobotInfoResponseByTargetCoordinate(
            coordinate: StatefulCoordinate,
        ): RobotInfoResponse {
            return RobotInfoResponse(
                coordinate.x,
                coordinate.y,
            )
        }
    }
}
