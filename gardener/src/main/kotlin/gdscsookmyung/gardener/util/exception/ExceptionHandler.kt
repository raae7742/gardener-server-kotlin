package gdscsookmyung.gardener.util.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [CustomException::class])
    fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(e.errorCode)
    }

}