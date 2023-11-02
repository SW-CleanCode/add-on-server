package com.cleancode.addonserver.service

import com.cleancode.addonserver.dto.request.MapRobotSetupRequest
import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.repository.RobotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RobotService(
    private val robotRepository: RobotRepository,
) {

    @Transactional
    fun createRobot(mapRobotSetupRequest: MapRobotSetupRequest): Robot {
        return robotRepository.save(mapRobotSetupRequest.getRobot())
    }
}
