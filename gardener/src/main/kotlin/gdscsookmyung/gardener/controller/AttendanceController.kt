package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.attendance.dto.AttendanceAttendeeDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceDateDto
import gdscsookmyung.gardener.service.AttendanceService
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.service.EventService
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "attendance", description = "출석 API")
@RestController
@RequestMapping("/attendance")
class AttendanceController(
    val attendanceService: AttendanceService,
    val eventService: EventService,
    val attendeeService: AttendeeService
) {

    @Operation(summary = "이벤트 출석부 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = AttendanceAttendeeDto::class)))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 객체 또는 연결된 객체를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @GetMapping
    fun readAllCheck(@Parameter(description = "이벤트 ID") @RequestParam eventId: Long): ResponseEntity<Any> {
        val event = eventService.readById(eventId)
        val attendees = attendeeService.readByEvent(event)
        val response = attendanceService.readAllByAttendee(event, attendees)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = response),
            HttpStatus.OK
        )
    }

    @Operation(summary = "이벤트의 오늘 출석 현황 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = AttendanceDateDto::class)))]),
        ApiResponse(responseCode = "401", description = "이미 지나간 또는 시작되지 않은 이벤트입니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 객체 또는 연결된 객체를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @GetMapping("/today")
    fun readTodayCheck(@Parameter(description = "이벤트 ID") @RequestParam eventId: Long): ResponseEntity<Any> {
        val event = eventService.readById(eventId)
        val attendees = attendeeService.readByEvent(event)
        val now = LocalDate.now()

        if (now.isBefore(event.startedAt)) throw CustomException(ErrorCode.EVENT_NOT_STARTED)
        else if (now.isAfter(event.endedAt)) throw CustomException(ErrorCode.EVENT_EXPIRED)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = attendanceService.readAllByEventAndDate(event, attendees, now)),
            HttpStatus.OK
        )
    }
}