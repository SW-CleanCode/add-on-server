package com.cleancode.addonserver.util

import com.cleancode.addonserver.dto.SpotDTO
import com.cleancode.addonserver.entity.Map
import com.cleancode.addonserver.error.exception.map.RouteNotFoundException
import org.springframework.stereotype.Component
import java.util.*

interface PathfindingAlgorithm {
    fun findPath(
        map: Map,
        start: SpotDTO,
        target: SpotDTO,
        hazards: Set<SpotDTO>,
    ): List<SpotDTO>
}

@Component
class BFSPathFinder : PathfindingAlgorithm {

    override fun findPath(
        map: Map,
        start: SpotDTO,
        target: SpotDTO,
        hazards: Set<SpotDTO>,
    ): List<SpotDTO> {
        val path: MutableList<SpotDTO> = mutableListOf()
        val visited = mutableSetOf<SpotDTO>()
        val visitedDirections = Array(map.width + 1) { IntArray(map.height + 1) }

        val queue = LinkedList<SpotDTO>()
        queue.offer(start)
        visited.add(start)

        val dx = intArrayOf(0, 0, 1, -1)
        val dy = intArrayOf(1, -1, 0, 0)

        while (queue.isNotEmpty()) {
            val currentSpot = queue.poll()
            val x = currentSpot.x
            val y = currentSpot.y

            if (x == target.x && y == target.y) {
                // 목표에 도달한 경우 경로를 역 추적하여 반환합니다.
                var currentX = x
                var currentY = y
                while (currentX != start.x || currentY != start.y) {
                    path.add(0, SpotDTO(currentX, currentY))
                    val direction = visitedDirections[currentX][currentY]
                    val nextX = currentX - dx[direction]
                    val nextY = currentY - dy[direction]
                    currentX = nextX
                    currentY = nextY
                }
                path.add(0, SpotDTO(start.x, start.y))
                return path
            }

            for (direction in 0 until 4) {
                val nextX = x + dx[direction]
                val nextY = y + dy[direction]
                val nextSpot = SpotDTO(nextX, nextY)

                if (isWithinBounds(nextX, nextY, map) &&
                    !visited.contains(nextSpot) &&
                    !hazards.contains(nextSpot)
                ) {
                    visited.add(nextSpot)
                    queue.offer(nextSpot)
                    visitedDirections[nextX][nextY] = direction
                }
            }
        }

        throw RouteNotFoundException()
    }

    private fun isWithinBounds(x: Int, y: Int, map: Map): Boolean {
        return x >= 0 && x <= map.width && y >= 0 && y <= map.height
    }
}
