package com.cleancode.addonserver.dto.response

import com.cleancode.addonserver.dto.SpotDTO

data class RobotImportantCoordinateVisitResponse(
    val robotPosition: RobotInfoResponse,
    val robotMovementHistory: List<SpotDTO>,
) {
    companion object {
        fun createRobotImportantCoordinateVisitResponse(
            robotPosition: RobotInfoResponse,
            robotMovementHistory: List<SpotDTO>,
        ): RobotImportantCoordinateVisitResponse {
            return RobotImportantCoordinateVisitResponse(
                robotPosition,
                robotMovementHistory,
            )
        }
    }
}
