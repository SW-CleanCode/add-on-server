package com.cleancode.addonserver.service

import com.cleancode.addonserver.dto.request.MapRobotSetupRequest
import com.cleancode.addonserver.dto.response.RobotInfoResponse
import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.error.exception.robot.InvalidRobotPositionException
import com.cleancode.addonserver.error.exception.robot.RobotNotFoundException
import com.cleancode.addonserver.repository.RobotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RobotService(
    private val robotRepository: RobotRepository,
) {

    fun getRobot(): Robot {
        return robotRepository.findAll().firstOrNull() ?: throw RobotNotFoundException()
    }

    @Transactional
    fun createRobot(mapRobotSetupRequest: MapRobotSetupRequest): Robot {
        validateRobotPosition(mapRobotSetupRequest)
        return robotRepository.save(mapRobotSetupRequest.getRobot())
    }

    @Transactional
    fun moveRobotToNearestImportantCoordinate(
        robotPosition: RobotInfoResponse,
    ) {
        val robot = robotRepository.findAll().firstOrNull() ?: throw RobotNotFoundException()
        robot.move(robotPosition.nowX, robotPosition.nowY)
    }

    private fun validateRobotPosition(
        mapRobotSetupRequest: MapRobotSetupRequest,
    ) {
        val (mapWidth, mapHeight, robotInitialX, robotInitialY) = mapRobotSetupRequest

        if ((robotInitialX < 0 || robotInitialX > mapWidth) ||
            (robotInitialY < 0 || robotInitialY > mapHeight)
        ) {
            throw InvalidRobotPositionException()
        }
    }
}
