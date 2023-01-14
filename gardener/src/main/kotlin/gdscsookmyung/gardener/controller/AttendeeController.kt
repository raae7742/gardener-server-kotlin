package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.attendee.dto.AttendeeRequestDto
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.util.exception.ErrorResponse
import gdscsookmyung.gardener.util.response.ResponseMessage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Tag(name = "attendee", description = "이벤트 참여자 API")
@Slf4j
@RestController
@RequestMapping("/event/{eventId}/attendee")
class AttendeeController(
    private val attendeeService: AttendeeService,
) {

    @Operation(summary = "이벤트 참가하기")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "참가하기 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = String::class)))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 객체 또는 연결된 객체를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping
    fun enterEvent(@Parameter(description = "이벤트 ID") @PathVariable eventId: Long,
                   @Parameter(description = "깃허브 ID") @RequestBody requestDto: AttendeeRequestDto,
                   request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        attendeeService.create(eventId, requestDto.github)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = ""),
            HttpStatus.OK
        )
    }

    @Operation(summary = "이벤트 나가기")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "나가기 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = String::class)))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 객체 또는 연결된 객체를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @DeleteMapping
    fun exitEvent(@Parameter(description = "이벤트 ID") @PathVariable eventId: Long,
                  @Parameter(description = "깃허브 ID") @RequestParam github: String,
                  request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        attendeeService.delete(eventId, github)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = ""),
            HttpStatus.OK
        )
    }
}