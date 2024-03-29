package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import gdscsookmyung.gardener.entity.event.dto.EventResponseDto
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.service.EventService
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

@Tag(name = "event", description = "이벤트 API")
@Slf4j
@RestController
@RequestMapping("/event")
class EventController (
    val eventService: EventService,
){
    @Operation(summary = "이벤트 생성")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "생성 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))])])
    @PostMapping
    fun createEvent(@Parameter(description = "생성할 이벤트 내용") @RequestBody requestDto: EventRequestDto): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventService.create(requestDto)),
            HttpStatus.OK
        )
    }

    @Operation(summary = "하나의 이벤트 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 이벤트를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])    @GetMapping("/{eventId}")
    fun readEvent(@PathVariable("eventId") eventId: Long): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventService.readById(eventId)),
            HttpStatus.OK
        )
    }

    @Operation(summary = "진행 중인 이벤트 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))])])
    @GetMapping("/current")
    fun readCurrentEvents(): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventService.readCurrentEvents()),
            HttpStatus.OK
        )
    }

    @Operation(summary = "지나간 이벤트 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))])])
    @GetMapping("/past")
    fun readPastEvents(): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventService.readPastEvents()),
            HttpStatus.OK
        )
    }

    @Operation(summary = "예정된 이벤트 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))])])
    @GetMapping("/future")
    fun readFutureEvents(): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventService.readFutureEvents()),
            HttpStatus.OK
        )
    }

    @Operation(summary = "이벤트 수정")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "수정 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 이벤트를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PatchMapping
    fun update(@Parameter(description = "이벤트 ID") @RequestParam eventId: Long,
               @Parameter(description = "수정할 이벤트 내용") @RequestBody requestDto: EventRequestDto
    ): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventService.update(eventId, requestDto)),
            HttpStatus.OK
        )
    }

    @Operation(summary = "이벤트 삭제")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "삭제 성공", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = Long::class)))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 이벤트를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @DeleteMapping
    fun delete(@Parameter(description = "이벤트 ID") @RequestParam eventId: Long): ResponseEntity<ResponseMessage> {
        eventService.delete(eventId)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = eventId),
            HttpStatus.OK
        )
    }
}