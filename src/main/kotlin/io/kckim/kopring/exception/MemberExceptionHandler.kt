package io.kckim.kopring.exception

import io.kckim.kopring.dto.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class MemberExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException::class)
    fun memberNotFoundException(
        e: MemberNotFoundException
    ): GenericResponse<Void> {
        return GenericResponse(
            message = e.message!!
        )
    }
}