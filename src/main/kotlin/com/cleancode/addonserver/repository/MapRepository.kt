package com.cleancode.addonserver.repository

import com.cleancode.addonserver.entity.Map
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MapRepository : JpaRepository<Map, UUID>
