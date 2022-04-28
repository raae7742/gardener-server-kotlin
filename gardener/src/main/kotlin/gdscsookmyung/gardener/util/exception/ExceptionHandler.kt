package gdscsookmyung.gardener.util.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [CustomException::class])
    fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(e.errorCode)
    }

}