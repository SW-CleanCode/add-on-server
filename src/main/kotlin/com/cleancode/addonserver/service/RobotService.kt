package com.cleancode.addonserver.service

import com.cleancode.addonserver.repository.RobotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RobotService(
    private val robotRepository: RobotRepository,
)
