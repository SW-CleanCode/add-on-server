package com.cleancode.addonserver.repository

import com.cleancode.addonserver.entity.StatefulCoordinate
import org.springframework.data.jpa.repository.JpaRepository

interface StatefulCoordinateRepository : JpaRepository<StatefulCoordinate, Long>
