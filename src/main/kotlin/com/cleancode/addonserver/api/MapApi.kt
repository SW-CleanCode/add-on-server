package com.cleancode.addonserver.api

import com.cleancode.addonserver.service.MapService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/map")
class MapApi(
    private val mapService: MapService,
)
