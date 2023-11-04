package com.cleancode.addonserver.service

import com.cleancode.addonserver.dto.SpotDTO
import com.cleancode.addonserver.dto.request.MapRobotSetupRequest
import com.cleancode.addonserver.dto.request.MapStatefulCoordinateRegisterRequest
import com.cleancode.addonserver.dto.response.RandomEventResponse
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
import com.cleancode.addonserver.util.RandomGenerator
import org.springframework.beans.factory.annotation.Value
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
    private val randomGenerator: RandomGenerator,
    @Value("\${event.hazard.probability}")
    private val hazardEventProbability: Double,
    @Value("\${event.important.probability}")
    private val importantEventProbability: Double,
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

        statefulCoordinateRepository.saveAll(newStatefulCoordinates)
        return Pair(map, statefulCoordinateRepository.findAllByMap(map))
    }

    @Transactional
    fun getNearestImportantCoordinateAndVisitIt():
        Triple<List<SpotDTO>, RobotInfoResponse, RandomEventResponse> {
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

        val hazardDetectEventResponse = hazardDetectEvent(
            map,
            hazardStatefulCoordinates,
            movementPath,
        )
        val importDetectEventResponse = importDetectEvent(map, movementPath)
        val randomEventResponse = RandomEventResponse
            .createRandomEventResponse(
                hazardDetectEventResponse,
                importDetectEventResponse,
            )

        return Triple(
            movementPath,
            RobotInfoResponse.createRobotInfoResponseByTargetCoordinate(
                nearestImportantCoordinate,
            ),
            randomEventResponse,
        )
    }

    private fun hazardDetectEvent(
        map: Map,
        hazardStatefulCoordinates: List<StatefulCoordinate>,
        movementPath: MutableList<SpotDTO>,
    ): StatefulCoordinate? {
        if (movementPath.size < 4 || Math.random() > hazardEventProbability) return null

        val (unpredictableHazardSpot, index) =
            randomGenerator.getRandomElementAndIndexExcludeFirstAndLastInList(
                movementPath,
            )
        val unpredictableHazard = statefulCoordinateRepository.save(
            StatefulCoordinate.createNewStatefulCoordinates(
                unpredictableHazardSpot.x,
                unpredictableHazardSpot.y,
                Status.HAZARD,
                map,
            ),
        )
        val previousUnpredictableHazard = movementPath[index - 1]
        val nextUnpredictableHazard = movementPath[index + 1]

        val partialPathAgainstUnpredictableHazard =
            findRouteAgainstUnpredictableHazard(
                map,
                previousUnpredictableHazard,
                nextUnpredictableHazard,
                hazardStatefulCoordinates + listOf(unpredictableHazard),
            )
        partialPathAgainstUnpredictableHazard.removeFirst()
        partialPathAgainstUnpredictableHazard.removeLast()

        movementPath.removeAt(index)
        movementPath.addAll(index, partialPathAgainstUnpredictableHazard)

        return unpredictableHazard
    }

    private fun importDetectEvent(
        map: Map,
        movementPath: MutableList<SpotDTO>,
    ): StatefulCoordinate? {
        if (movementPath.size < 4 || Math.random() > importantEventProbability) {
            return null
        }

        val (unpredictableImportantSpot, index) =
            randomGenerator.getRandomElementAndIndexExcludeFirstAndLastInList(
                movementPath,
            )
        val unpredictableImportant = statefulCoordinateRepository.save(
            StatefulCoordinate.createNewStatefulCoordinates(
                unpredictableImportantSpot.x,
                unpredictableImportantSpot.y,
                Status.IMPORTANT,
                map,
            ),
        )
        unpredictableImportant.visit()
        return unpredictableImportant
    }

    private fun findRouteAgainstUnpredictableHazard(
        map: Map,
        startSpot: SpotDTO,
        targetSpot: SpotDTO,
        hazardStatefulCoordinates: List<StatefulCoordinate>,
    ): MutableList<SpotDTO> {
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

    private fun findRouteToNearestImportantCoordinateAvoidingHazard(
        map: Map,
        robot: Robot,
        nearestImportantCoordinate: StatefulCoordinate,
        hazardStatefulCoordinates: List<StatefulCoordinate>,
    ): MutableList<SpotDTO> {
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
