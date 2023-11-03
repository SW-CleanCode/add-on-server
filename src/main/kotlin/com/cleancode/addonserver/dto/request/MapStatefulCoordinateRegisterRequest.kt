package com.cleancode.addonserver.dto.request

import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.StatefulCoordinate
import com.cleancode.addonserver.entity.enum.Status
import io.swagger.v3.oas.annotations.media.Schema

data class MapStatefulCoordinateRegisterRequest(
    val importantSpots: List<Spot>,
    val hazardSpots: List<Spot>,
) {

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
}
