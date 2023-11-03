package com.cleancode.addonserver.error.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: String, val message: String, var status: Int) {
    // Common
    INVALID_INPUT_VALUE("C01", "Invalid Input Value.", HttpStatus.BAD_REQUEST.value()),
    METHOD_NOT_ALLOWED(
        "C02",
        "Invalid Method Type.",
        HttpStatus.METHOD_NOT_ALLOWED.value(),
    ),
    ENTITY_NOT_FOUND("C03", "Entity Not Found.", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_SERVER_ERROR(
        "C04",
        "Internal Server Error.",
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
    ),

    //Map
    MAP_NOT_FOUND("M01", "Map is Not Found.", HttpStatus.BAD_REQUEST.value()),
    STATEFUL_COORDINATE_NOT_FOUND(
        "M02",
        "Stateful Coordinate is Not Found.",
        HttpStatus.BAD_REQUEST.value(),
    ),
    NO_MORE_IMPORTANT_COORDINATE(
        "M03",
        "There is no more important coordinate.",
        HttpStatus.BAD_REQUEST.value(),
    ),
    ROUTE_NOT_FOUND("M04", "Route is Not Found.", HttpStatus.BAD_REQUEST.value()),

    //Robot
    INVALID_ROBOT_POSITION(
        "R01",
        "Robot Position is Invalid.",
        HttpStatus.BAD_REQUEST.value(),
    ),
    ROBOT_NOT_FOUND("R02", "Robot is Not Found.", HttpStatus.BAD_REQUEST.value()),

    // External API
    EXTERNAL_API_FAILED(
        "E01",
        "External API Request is failed.",
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
    ),
}
