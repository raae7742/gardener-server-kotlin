package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.auth.JwtTokenProvider
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.util.response.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/attendee")
class AttendeeController(
    private val attendeeService: AttendeeService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    /**
     * 프로젝트 나가기
     */
    @DeleteMapping
    fun exitEvent(@RequestHeader("Authorization") token: String, @RequestParam eventId: Long): ResponseEntity<ResponseMessage> {
        val username = jwtTokenProvider.getUsername(token)
        attendeeService.delete(eventId, username)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = ""),
            HttpStatus.OK
        )
    }
}