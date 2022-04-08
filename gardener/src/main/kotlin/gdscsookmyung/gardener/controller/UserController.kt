package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.user.dto.LoginRequestDto
import gdscsookmyung.gardener.entity.user.dto.LoginResponseDto
import gdscsookmyung.gardener.entity.user.dto.UserRequestDto
import gdscsookmyung.gardener.service.UserService
import gdscsookmyung.gardener.util.response.ResponseMessage
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
class UserController(
    private val userService: UserService
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
}