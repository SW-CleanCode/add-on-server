package com.cleancode.addonserver.error

import com.cleancode.addonserver.error.exception.BusinessException
import com.cleancode.addonserver.error.exception.EntityNotFoundException
import com.cleancode.addonserver.error.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(
        exception: BusinessException,
    ): ResponseEntity<ErrorResponse> {
        logger.error(exception.errorCode.message)
        val errorCode = exception.errorCode
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFoundException(
        exception: EntityNotFoundException,
    ): ResponseEntity<ErrorResponse> {
        logger.error(exception.errorCode.message)
        val errorCode = exception.errorCode
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(
        exception: HttpRequestMethodNotSupportedException?,
    ): ResponseEntity<ErrorResponse> {
        logger.error("HttpRequestMethod is not Supported.")
        val errorCode = ErrorCode.METHOD_NOT_ALLOWED
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException?,
    ): ResponseEntity<ErrorResponse> {
        logger.error("MethodArgument is Invalid.")
        val errorCode = ErrorCode.INVALID_INPUT_VALUE
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException?,
    ): ResponseEntity<ErrorResponse> {
        logger.error("HttpMessage is not Readable.")
        val errorCode = ErrorCode.INVALID_INPUT_VALUE
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleHttpMessageNotReadableException(
        exception: MethodArgumentTypeMismatchException,
    ):
        ResponseEntity<ErrorResponse> {
        logger.error("MethodArgument's Type is Mismatched.")
        val errorCode = ErrorCode.INVALID_INPUT_VALUE
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected Exception occurs.", exception)
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ErrorResponse(errorCode)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
