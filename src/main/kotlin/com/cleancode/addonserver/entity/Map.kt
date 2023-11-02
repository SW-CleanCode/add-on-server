package com.cleancode.addonserver.entity

import jakarta.persistence.*
import java.util.*

@Entity
class Map(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,

    @Column(nullable = false)
    var width: Int,

    @Column(nullable = false)
    var height: Int,

    @OneToMany(mappedBy = "map")
    var statefulCoordinates: MutableList<StatefulCoordinate> = mutableListOf(),
) {
    companion object {
        fun createNewMap(width: Int, height: Int): Map {
            return Map(
                width = width,
                height = height,
            )
        }
    }
}
