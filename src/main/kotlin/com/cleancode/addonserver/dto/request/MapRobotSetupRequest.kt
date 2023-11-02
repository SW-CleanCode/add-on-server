package com.cleancode.addonserver.dto.request

import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.entity.StatefulCoordinate
import com.cleancode.addonserver.entity.enum.Status
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class Spot(
    @field:NotNull
    val x: Int,

    @field:NotNull
    val y: Int,
)

data class MapRobotSetupRequest(
    @field:NotNull
    val mapWidth: Int,

    @field:NotNull
    val mapHeight: Int,

    @field:NotNull
    val robotInitialX: Int,

    @field:NotNull
    val robotInitialY: Int,

    val importantSpots: List<Spot>,
    val hazardSpots: List<Spot>,
) {

    @Schema(hidden = true)
    fun getMap(): Map {
        return Map.createNewMap(mapWidth, mapHeight)
    }

    @Schema(hidden = true)
    fun getStatefulCoordinates(
        createdMap: Map,
    ): List<StatefulCoordinate> {
        val hazardCoordinates = hazardSpots.map {
            StatefulCoordinate.createNewStatefulCoordinates(
                it.x,
                it.y,
                Status.HAZARD,
                createdMap,
            )
        }

        val importantCoordinates = importantSpots.map {
            StatefulCoordinate.createNewStatefulCoordinates(
                it.x,
                it.y,
                Status.IMPORTANT,
                createdMap,
            )
        }

        return hazardCoordinates + importantCoordinates
    }

    @Schema(hidden = true)
    fun getRobot(): Robot {
        return Robot.createNewRobot(robotInitialX, robotInitialY)
    }
}
