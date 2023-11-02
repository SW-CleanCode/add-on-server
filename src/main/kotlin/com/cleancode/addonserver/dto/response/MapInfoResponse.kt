package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.dto.CoordinateDTO
import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.StatefulCoordinate

data class MapInfoResponse(
    val width: Int,
    val height: Int,
    val statefulCoordinates: List<CoordinateDTO>,
) {

    companion object {
        fun createMapInfoResponse(
            map: Map,
            statefulCoordinates: List<StatefulCoordinate>,
        ): MapInfoResponse {
            return MapInfoResponse(
                map.width,
                map.height,
                statefulCoordinates.map { CoordinateDTO.createCoordinatesDTO(it) },
            )
        }
    }
}
