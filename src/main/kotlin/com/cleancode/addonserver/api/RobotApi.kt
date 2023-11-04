package com.cleancode.addonserver.api

import com.cleancode.addonserver.dto.response.RobotImportantCoordinateVisitResponse
import com.cleancode.addonserver.dto.response.RobotInfoResponse
import com.cleancode.addonserver.service.MapService
import com.cleancode.addonserver.service.RobotService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/robot")
class RobotApi(
    private val mapService: MapService,
    private val robotService: RobotService,
) {

    @Operation(summary = "로봇 위치 정보 반환")
    @GetMapping
    fun getRobotInfo(): ResponseEntity<RobotInfoResponse> {
        val robot = robotService.getRobot()
        return ResponseEntity.ok()
            .body(RobotInfoResponse.createRobotInfoResponse(robot))
    }

    @Operation(summary = "현재 위치에서 가장 가까운 탐색 지점으로 로봇 위치 이동")
    @PostMapping("/move")
    fun moveRobotToNearestPoint(): ResponseEntity<RobotImportantCoordinateVisitResponse> {
        val (movementPath, robotInfoResponse, randomEventResponse) =
            mapService.getNearestImportantCoordinateAndVisitIt()
        robotService.moveRobotToNearestImportantCoordinate(robotInfoResponse)

        return ResponseEntity.ok()
            .body(
                RobotImportantCoordinateVisitResponse
                    .createRobotImportantCoordinateVisitResponse(
                        robotInfoResponse,
                        movementPath,
                        randomEventResponse,
                    ),
            )
    }
}
