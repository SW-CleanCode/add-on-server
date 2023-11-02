package com.cleancode.addonserver.repository

import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.StatefulCoordinate
import org.springframework.data.jpa.repository.JpaRepository

interface StatefulCoordinateRepository : JpaRepository<StatefulCoordinate, Long> {
    fun findAllByMap(map: Map): List<StatefulCoordinate>
}
