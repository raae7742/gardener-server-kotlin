package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.attendance.dto.AttendanceAttendeeDto
import gdscsookmyung.gardener.entity.attendance.dto.AttendanceDateDto
import gdscsookmyung.gardener.service.AttendanceService
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
@RequestMapping("/event/{eventId}/attendance")
class AttendanceController(
    val attendanceService: AttendanceService,
) {

    @Operation(summary = "이벤트 출석부 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = AttendanceAttendeeDto::class)))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 객체 또는 연결된 객체를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @GetMapping("/all")
    fun readAllAttendances(@Parameter(description = "이벤트 ID") @PathVariable eventId: Long): ResponseEntity<Any> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = attendanceService.readAllByEventId(eventId)),
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
    fun readTodayAttendances(@Parameter(description = "이벤트 ID") @PathVariable eventId: Long): ResponseEntity<Any> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = attendanceService.readAllByEventIdAndDate(eventId, LocalDate.now())),
            HttpStatus.OK
        )
    }
}