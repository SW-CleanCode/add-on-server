package com.cleancode.addonserver.entity

import jakarta.persistence.*
import java.util.*

@Entity
class Robot(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    var nowX: Int,

    @Column(nullable = false)
    var nowY: Int,
)
