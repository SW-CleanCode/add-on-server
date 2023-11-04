package com.cleancode.addonserver.util

import org.springframework.stereotype.Component

@Component
class RandomGenerator {

    fun<T> getRandomElementAndIndexExcludeFirstAndLastInList(list: List<T>):
        Pair<T, Int> {
        val randomIndex = (1 until list.size - 1).shuffled().first()
        return Pair(list[randomIndex], randomIndex)
    }
}
