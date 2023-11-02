package com.cleancode.addonserver.entity

import com.cleancode.addonserver.entity.enum.Status
import jakarta.persistence.*

@Entity
class StatefulCoordinate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false)
    var x: Int,

    @Column(nullable = false)
    var y: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status,

    @Column(nullable = false)
    var isVisited: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var map: Map? = null,
) {
    companion object {
        fun createNewStatefulCoordinates(x: Int, y: Int, status: Status, map: Map):
            StatefulCoordinate {
            return StatefulCoordinate(
                x = x,
                y = y,
                status = status,
                map = map,
            )
        }
    }
}
