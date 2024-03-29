package gdscsookmyung.gardener.controller

import gdscsookmyung.gardener.auth.JwtTokenProvider
import gdscsookmyung.gardener.entity.event.dto.EventResponseDto
import gdscsookmyung.gardener.entity.user.dto.LoginRequestDto
import gdscsookmyung.gardener.entity.user.dto.LoginResponseDto
import gdscsookmyung.gardener.entity.user.dto.TokenRequestDto
import gdscsookmyung.gardener.entity.user.dto.UserRequestDto
import gdscsookmyung.gardener.service.EventService
import gdscsookmyung.gardener.service.UserService
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
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "user", description = "유저 API")
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
class UserController(
    private val userService: UserService,
    private val eventService: EventService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Operation(summary = "회원가입")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입 & 로그인 성공", content = [
                Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "409", description = "이미 같은 깃헙 아이디를 사용하는 유저가 존재합니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
        ApiResponse(responseCode = "409", description = "이미 같은 이름을 사용하는 유저가 존재합니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping("/join")
    fun join(@Parameter(description = "가입할 유저 정보") @RequestBody requestDto: UserRequestDto): ResponseEntity<ResponseMessage> {
        val user = userService.join(requestDto)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = user),
            HttpStatus.OK
        )
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 틀렸습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping("/login")
    fun login(@Parameter(description = "유저의 ID & Password") @RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<ResponseMessage> {
        val user = userService.login(loginRequestDto)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = user),
            HttpStatus.OK
        )
    }

    @Operation(summary = "유저가 참여한/참여중인 이벤트")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @GetMapping("/event")
    fun readEvents(@Parameter(description = "JWT 토큰") @RequestHeader("Authorization") token: String): ResponseEntity<ResponseMessage> {
        val username = jwtTokenProvider.getUsername(token)
        val events = eventService.readUserEvents(username)

        return ResponseEntity(
            ResponseMessage(message = "성공", data = events),
            HttpStatus.OK
        )
    }

    @Operation(summary = "유저네임 수정")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "조회 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PutMapping()
    fun editUsername(@Parameter(description = "유저 ID") @RequestParam userId: Long, @Parameter(description = "새로 바꿀 유저네임") @RequestParam username: String): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = userService.updateUsername(userId, username)),
            HttpStatus.OK
        )
    }

    @Operation(summary = "액세스 토큰 재발급")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "재발급 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = EventResponseDto::class))))]),
        ApiResponse(responseCode = "401", description = "아직 만료되지 않은 토큰입니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
        ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
        ApiResponse(responseCode = "406", description = "접근이 거부되었습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody tokenRequestDto: TokenRequestDto) : ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = userService.refreshToken(tokenRequestDto)),
            HttpStatus.OK
        )
    }

    @Operation(summary = "로그아웃")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "로그아웃 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = String::class))))]),
        ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<ResponseMessage> {
        return ResponseEntity(
            ResponseMessage(message = "성공", data = userService.logout(token)),
            HttpStatus.OK
        )
    }
    @PutMapping("/password")
    fun editPassword() {
        //TODO
    }
}