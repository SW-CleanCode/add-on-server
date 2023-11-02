package com.cleancode.addonserver.api

import com.cleancode.addonserver.dto.response.RobotInfoResponse
import com.cleancode.addonserver.service.RobotService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/robot")
class RobotApi(
    private val robotService: RobotService,
) {

    @Operation(summary = "로봇 위치 정보 반환")
    @GetMapping
    fun getRobotInfo(): ResponseEntity<RobotInfoResponse> {
        val robot = robotService.getRobot()
        return ResponseEntity.ok()
            .body(RobotInfoResponse.createRobotInfoResponse(robot))
    }
}
