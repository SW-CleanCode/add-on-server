package com.cleancode.addonserver.util

import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.entity.StatefulCoordinate
import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class DistanceCalculator {

    fun getDistanceFromRobotToCoordinate(
        robot: Robot,
        statefulCoordinate: StatefulCoordinate,
    ): Int {
        val robotX = robot.x
        val robotY = robot.y

        val coordinateX = statefulCoordinate.x
        val coordinateY = statefulCoordinate.y

        return getDistance(robotX, robotY, coordinateX, coordinateY)
    }

    private fun getDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return abs(x1 - x2) + abs(y1 - y2)
    }
}
