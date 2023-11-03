package com.cleancode.addonserver.service

import com.cleancode.addonserver.dto.SpotDTO
import com.cleancode.addonserver.dto.request.MapRobotSetupRequest
import com.cleancode.addonserver.dto.request.MapStatefulCoordinateRegisterRequest
import com.cleancode.addonserver.dto.response.RobotInfoResponse
import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.entity.Robot
import com.cleancode.addonserver.entity.StatefulCoordinate
import com.cleancode.addonserver.entity.enum.Status
import com.cleancode.addonserver.error.exception.map.MapNotFoundException
import com.cleancode.addonserver.error.exception.map.NoMoreImportantCoordinateException
import com.cleancode.addonserver.error.exception.map.StatefulCoordinateNotFoundException
import com.cleancode.addonserver.error.exception.robot.RobotNotFoundException
import com.cleancode.addonserver.repository.MapRepository
import com.cleancode.addonserver.repository.RobotRepository
import com.cleancode.addonserver.repository.StatefulCoordinateRepository
import com.cleancode.addonserver.util.BFSPathFinder
import com.cleancode.addonserver.util.DistanceCalculator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MapService(
    private val mapRepository: MapRepository,
    private val statefulCoordinateRepository: StatefulCoordinateRepository,
    private val robotRepository: RobotRepository,
    private val distanceCalculator: DistanceCalculator,
    private val bfsPathFinder: BFSPathFinder,
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

    @Transactional
    fun addStatefulCoordinates(
        mapStatefulCoordinateRegisterRequest: MapStatefulCoordinateRegisterRequest,
    ): Pair<Map, List<StatefulCoordinate>> {
        val map = mapRepository.findAll().firstOrNull() ?: throw MapNotFoundException()
        val newStatefulCoordinates = mapStatefulCoordinateRegisterRequest
            .getStatefulCoordinates(map)
        // 좌표 중복 삽입 방지 추가 필요
        statefulCoordinateRepository.saveAll(newStatefulCoordinates)
        return Pair(map, statefulCoordinateRepository.findAllByMap(map))
    }

    @Transactional
    fun getNearestImportantCoordinateAndVisitIt():
        Pair<List<SpotDTO>, RobotInfoResponse> {
        val map = mapRepository.findAll().firstOrNull() ?: throw RobotNotFoundException()
        val statefulCoordinates = statefulCoordinateRepository.findAllByMap(map)
        val (hazardStatefulCoordinates, unvisitedImportantCoordinates) =
            separateCoordinatesByStatus(statefulCoordinates)

        val robot = robotRepository.findAll().firstOrNull()
            ?: throw RobotNotFoundException()
        val nearestImportantCoordinate = getNearestImportantCoordinate(
            robot,
            unvisitedImportantCoordinates,
        )

        val movementPath = findRouteToNearestImportantCoordinateAvoidingHazard(
            map,
            robot,
            nearestImportantCoordinate,
            hazardStatefulCoordinates,
        )
        nearestImportantCoordinate.visit()

        return Pair(
            movementPath,
            RobotInfoResponse.createRobotInfoResponseByTargetCoordinate(
                nearestImportantCoordinate,
            ),
        )
    }

    private fun findRouteToNearestImportantCoordinateAvoidingHazard(
        map: Map,
        robot: Robot,
        nearestImportantCoordinate: StatefulCoordinate,
        hazardStatefulCoordinates: List<StatefulCoordinate>,
    ): List<SpotDTO> {
        val startSpot = SpotDTO.createSpotDTO(robot.x, robot.y)
        val targetSpot = SpotDTO.createSpotDTO(
            nearestImportantCoordinate.x,
            nearestImportantCoordinate.y,
        )
        val hazards = hazardStatefulCoordinates.map {
            SpotDTO.createSpotDTO(it.x, it.y)
        }

        return bfsPathFinder.findPath(
            map,
            startSpot,
            targetSpot,
            hazards.toSet(),
        )
    }

    private fun getNearestImportantCoordinate(
        robot: Robot,
        unvisitedImportantCoordinates: List<StatefulCoordinate>,
    ): StatefulCoordinate {
        val nearestImportantCoordinate = unvisitedImportantCoordinates.minByOrNull {
            distanceCalculator.getDistanceFromRobotToCoordinate(robot, it)
        } ?: throw StatefulCoordinateNotFoundException()

        return nearestImportantCoordinate
    }

    private fun separateCoordinatesByStatus(
        statefulCoordinates: List<StatefulCoordinate>,
    ): Pair<List<StatefulCoordinate>, List<StatefulCoordinate>> {
        val hazardStatefulCoordinates = statefulCoordinates.filter {
            it.status == Status.HAZARD
        }
        val unvisitedImportantCoordinates = statefulCoordinates.filter {
            !it.isVisited && it.status == Status.IMPORTANT
        }
        if (unvisitedImportantCoordinates.isEmpty()) {
            throw NoMoreImportantCoordinateException()
        }

        return Pair(hazardStatefulCoordinates, unvisitedImportantCoordinates)
    }
}
