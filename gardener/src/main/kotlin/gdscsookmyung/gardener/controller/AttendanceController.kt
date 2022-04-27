package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.service.AttendanceService
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.service.EventService
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import gdscsookmyung.gardener.util.response.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/attendance")
class AttendanceController(
    val attendanceService: AttendanceService,
    val eventService: EventService,
    val attendeeService: AttendeeService
) {

    @GetMapping
    fun readAllCheck(@RequestParam eventId: Long): ResponseEntity<Any> {
        val event = eventService.readById(eventId)
        val attendees = attendeeService.readByEvent(event)
        val response = attendanceService.readAllByAttendee(event, attendees)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = response),
            HttpStatus.OK
        )
    }

    @GetMapping("/today")
    fun readTodayCheck(@RequestParam eventId: Long): ResponseEntity<Any> {
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