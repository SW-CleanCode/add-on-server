package com.cleancode.addonserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AddOnServerApplication

fun main(args: Array<String>) {
    runApplication<AddOnServerApplication>(*args)
}
