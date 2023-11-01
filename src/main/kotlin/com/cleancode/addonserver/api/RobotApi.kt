package com.cleancode.addonserver.api

import com.cleancode.addonserver.service.RobotService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/robot")
class RobotApi(
    private val robotService: RobotService,
)
