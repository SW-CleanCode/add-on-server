package com.cleancode.addonserver.repository

import com.cleancode.addonserver.entity.Robot
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RobotRepository : JpaRepository<Robot, UUID>
