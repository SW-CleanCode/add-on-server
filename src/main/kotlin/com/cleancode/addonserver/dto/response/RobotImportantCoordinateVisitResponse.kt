package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.dto.SpotDTO
import com.cleancode.addonserver.entity.StatefulCoordinate

data class RobotImportantCoordinateVisitResponse(
    val robotPosition: RobotInfoResponse,
    val robotMovementHistory: List<SpotDTO>,
    val unpredictableHazard: SpotDTO?,
    val unpredictableImportant: SpotDTO?,
) {
    companion object {
        fun createRobotImportantCoordinateVisitResponse(
            robotPosition: RobotInfoResponse,
            robotMovementHistory: List<SpotDTO>,
            randomEventResponse: RandomEventResponse,
        ): RobotImportantCoordinateVisitResponse {
            return RobotImportantCoordinateVisitResponse(
                robotPosition,
                robotMovementHistory,
                randomEventResponse.hazardSpotDTO,
                randomEventResponse.importantSpotDTO,
            )
        }
    }
}

data class RandomEventResponse(
    val hazardSpotDTO: SpotDTO?,
    val importantSpotDTO: SpotDTO?,
) {
    companion object {
        fun createRandomEventResponse(
            hazardCoordinate: StatefulCoordinate?,
            importantCoordinate: StatefulCoordinate?,
        ): RandomEventResponse {
            return RandomEventResponse(
                hazardCoordinate?.let { SpotDTO.createSpotDTO(it.x, it.y) },
                importantCoordinate?.let { SpotDTO.createSpotDTO(it.x, it.y) },
            )
        }
    }
}
