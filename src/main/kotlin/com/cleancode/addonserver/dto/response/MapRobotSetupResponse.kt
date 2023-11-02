package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.entity.StatefulCoordinate

data class MapRobotSetupResponse(
    val mapWidth: Int,
    val mapHeight: Int,
    val robotX: Int,
    val robotY: Int,
    val statefulCoordinates: List<StatefulCoordinate>,
) {

    companion object {
        fun createMapRobotSetupResponse(
            map: Map,
            robot: Robot,
            statefulCoordinates: List<StatefulCoordinate>,
        ): MapRobotSetupResponse {
            return MapRobotSetupResponse(
                map.width,
                map.height,
                robot.nowX,
                robot.nowY,
                statefulCoordinates,
            )
        }
    }
}
