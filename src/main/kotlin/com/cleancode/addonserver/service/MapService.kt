package com.cleancode.addonserver.service

import com.cleancode.addonserver.dto.request.MapRobotSetupRequest
import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.StatefulCoordinate
import com.cleancode.addonserver.error.exception.map.MapNotFoundException
import com.cleancode.addonserver.repository.MapRepository
import com.cleancode.addonserver.repository.StatefulCoordinateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MapService(
    private val mapRepository: MapRepository,
    private val statefulCoordinateRepository: StatefulCoordinateRepository,
) {

    fun getMapAndStatefulCoordinates(): Pair<Map, List<StatefulCoordinate>> {
        val map = mapRepository.findAll().firstOrNull() ?: throw MapNotFoundException()
        val statefulCoordinates = statefulCoordinateRepository.findAllByMap(map)

        return Pair(map, statefulCoordinates)
    }

    @Transactional
    fun createMapAndStatefulCoordinates(mapRobotSetupRequest: MapRobotSetupRequest):
        Pair<Map, List<StatefulCoordinate>> {
        val createdMap = mapRepository.save(
            mapRobotSetupRequest.getMap(),
        )

        val createdStatefulCoordinates =
            statefulCoordinateRepository.saveAll(
                mapRobotSetupRequest.getStatefulCoordinates(createdMap),
            )

        return Pair(createdMap, createdStatefulCoordinates)
    }
}
