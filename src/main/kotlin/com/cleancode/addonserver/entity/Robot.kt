package com.cleancode.addonserver.entity

import jakarta.persistence.*
import java.util.*

@Entity
class Robot(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    var x: Int,

    @Column(nullable = false)
    var y: Int,
) {
    companion object {
        fun createNewRobot(nowX: Int, nowY: Int): Robot {
            return Robot(
                x = nowX,
                y = nowY,
            )
        }
    }
}
