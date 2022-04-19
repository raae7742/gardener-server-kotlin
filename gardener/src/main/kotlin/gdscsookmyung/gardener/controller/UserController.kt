package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.auth.JwtTokenProvider
import gdscsookmyung.gardener.entity.user.dto.LoginRequestDto
import gdscsookmyung.gardener.entity.user.dto.UserRequestDto
import gdscsookmyung.gardener.service.EventService
import gdscsookmyung.gardener.service.UserService
import gdscsookmyung.gardener.util.response.ResponseMessage
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
class UserController(
    private val userService: UserService,
    private val eventService: EventService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/join")
    fun join(@RequestBody requestDto: UserRequestDto): ResponseEntity<ResponseMessage> {
        val user = userService.join(requestDto)


        return ResponseEntity(
            ResponseMessage(message = "성공", data = user),
            HttpStatus.OK
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<ResponseMessage> {
        val user = userService.login(loginRequestDto)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = user),
            HttpStatus.OK
        )
    }

    @GetMapping("/event")
    fun readEvents(@RequestHeader("Authorization") token: String): ResponseEntity<ResponseMessage> {
        val username = jwtTokenProvider.getUsername(token)
        val events = eventService.readUserEvents(username)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = events),
            HttpStatus.OK
        )
    }

    @PutMapping()
    fun editUsername(@RequestParam id: Long, @RequestParam username: String): ResponseEntity<ResponseMessage> {
        val user = userService.updateUsername(id, username)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = user),
            HttpStatus.OK
        )
    }

    @PutMapping("/password")
    fun editPassword() {
        //TODO
    }
}