package com.cleancode.addonserver.api

import com.cleancode.addonserver.dto.request.MapRobotSetupRequest
import com.cleancode.addonserver.dto.response.MapRobotSetupResponse
import com.cleancode.addonserver.service.MapService
import com.cleancode.addonserver.service.RobotService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/map")
class MapApi(
    private val mapService: MapService,
    private val robotService: RobotService,
) {

    @Operation(
        summary = "맵 생성 및 로봇 생성",
    )
    @PostMapping("")
    fun createMap(
        @RequestBody @Valid
        mapRobotSetupRequest: MapRobotSetupRequest,
    ): ResponseEntity<MapRobotSetupResponse> {
        val (map, statefulCoordinates) =
            mapService.createMapAndStatefulCoordinates(mapRobotSetupRequest)
        val robot = robotService.createRobot(
            mapRobotSetupRequest,
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                MapRobotSetupResponse.createMapRobotSetupResponse(
                    map,
                    robot,
                    statefulCoordinates,
                ),
            )
    }
}
