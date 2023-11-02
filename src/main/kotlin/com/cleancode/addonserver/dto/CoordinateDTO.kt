package com.cleancode.addonserver.dto

import com.cleancode.addonserver.entity.StatefulCoordinate
import com.cleancode.addonserver.entity.enum.Status

data class CoordinateDTO(
    val x: Int,
    val y: Int,
    val status: Status,
    val isVisited: Boolean,
) {
    companion object {
        fun createCoordinatesDTO(
            statefulCoordinate: StatefulCoordinate,
        ): CoordinateDTO {
            return CoordinateDTO(
                statefulCoordinate.x,
                statefulCoordinate.y,
                statefulCoordinate.status,
                statefulCoordinate.isVisited,
            )
        }
    }
}
