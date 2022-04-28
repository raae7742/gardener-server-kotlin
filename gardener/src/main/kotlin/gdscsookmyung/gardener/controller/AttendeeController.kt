package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.auth.JwtTokenProvider
import gdscsookmyung.gardener.entity.event.dto.EventResponseDto
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.util.exception.ErrorResponse
import gdscsookmyung.gardener.util.response.ResponseMessage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "attendee", description = "이벤트 참여자 API")
@RestController
@RequestMapping("/attendee")
class AttendeeController(
    private val attendeeService: AttendeeService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Operation(summary = "프로젝트 나가기")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "나가기 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = String::class)))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 객체 또는 연결된 객체를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @DeleteMapping
    fun exitEvent(@Parameter(description = "JWT 토큰") @RequestHeader("Authorization") token: String, @Parameter(description = "이벤트 ID") @RequestParam eventId: Long): ResponseEntity<ResponseMessage> {
        val username = jwtTokenProvider.getUsername(token)
        attendeeService.delete(eventId, username)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = ""),
            HttpStatus.OK
        )
    }
}