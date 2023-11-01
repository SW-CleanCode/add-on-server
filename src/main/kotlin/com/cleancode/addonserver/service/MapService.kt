package com.cleancode.addonserver.service

import com.cleancode.addonserver.repository.MapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MapService(
    private val mapRepository: MapRepository,
)
