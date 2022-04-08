package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import gdscsookmyung.gardener.service.EventService
import gdscsookmyung.gardener.util.response.ResponseMessage
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequiredArgsConstructor
@RequestMapping("/event")
class EventController (
    val eventService: EventService
){
    @PostMapping
    fun createEvent(@RequestBody requestDto: EventRequestDto): ResponseEntity<ResponseMessage> {
        val event = eventService.create(requestDto)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = event),
            HttpStatus.OK
        )
    }

    @GetMapping("/current")
    fun readCurrentEvents(): ResponseEntity<ResponseMessage> {
        val currentEvents = eventService.readCurrentEvents()

        return ResponseEntity(
            ResponseMessage(message = "성공", data = currentEvents),
            HttpStatus.OK
        )
    }

    @GetMapping("/past")
    fun readPastEvents(): ResponseEntity<ResponseMessage> {
        val pastEvents = eventService.readPastEvents()

        return ResponseEntity(
            ResponseMessage(message = "성공", data = pastEvents),
            HttpStatus.OK
        )
    }

    @GetMapping("/future")
    fun readFutureEvents(): ResponseEntity<ResponseMessage> {
        val futureEvents = eventService.readFutureEvents()

        return ResponseEntity(
            ResponseMessage(message = "성공", data = futureEvents),
            HttpStatus.OK
        )
    }

    @PutMapping
    fun update(@RequestParam id: Long, @RequestBody requestDto: EventRequestDto): ResponseEntity<ResponseMessage> {
        val event = eventService.update(id, requestDto)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = event),
            HttpStatus.OK
        )
    }

    @DeleteMapping
    fun delete(@RequestParam id: Long): ResponseEntity<ResponseMessage> {
        eventService.delete(id)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = id),
            HttpStatus.OK
        )
    }
}