package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.entity.user.dto.LoginRequestDto
import gdscsookmyung.gardener.entity.user.dto.LoginResponseDto
import gdscsookmyung.gardener.entity.user.dto.UserRequestDto
import gdscsookmyung.gardener.service.UserService
import lombok.RequiredArgsConstructor
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
    fun join(@RequestBody requestDto: UserRequestDto): LoginResponseDto {
        return userService.join(requestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): LoginResponseDto {
        return userService.login(loginRequestDto)
    }
}