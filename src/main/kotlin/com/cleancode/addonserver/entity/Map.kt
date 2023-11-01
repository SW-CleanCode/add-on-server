package com.cleancode.addonserver.entity

import jakarta.persistence.*
import java.util.*

@Entity
class Map(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    var maxX: Int,

    @Column(nullable = false)
    var maxY: Int,

    @OneToMany(mappedBy = "map")
    var statefulCoordinates: List<StatefulCoordinates> = mutableListOf(),
)
