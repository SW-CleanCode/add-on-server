package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.dto.CoordinateDTO
import com.cleancode.addonserver.entity.StatefulCoordinate

data class MapCoordinateRegisterResponse(
    val statefulCoordinates: List<CoordinateDTO>,
) {
    companion object {
        fun createMapCoordinateRegisterResponse(
            statefulCoordinates: List<StatefulCoordinate>,
        ): MapCoordinateRegisterResponse {
            return MapCoordinateRegisterResponse(
                statefulCoordinates.map { CoordinateDTO.createCoordinatesDTO(it) },
            )
        }
    }
}
