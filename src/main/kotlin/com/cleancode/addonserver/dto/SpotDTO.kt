package com.cleancode.addonserver.dto

data class SpotDTO(
    val x: Int,
    val y: Int,
) {
    companion object {
        fun createSpotDTO(x: Int, y: Int): SpotDTO {
            return SpotDTO(x, y)
        }
    }
}
