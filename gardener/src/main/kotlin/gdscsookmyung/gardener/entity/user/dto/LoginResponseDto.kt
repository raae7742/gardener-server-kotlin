package gdscsookmyung.gardener.entity.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class LoginResponseDto (

    @Schema(description = "유저 ID")
    val id: Long,

    @Schema(description = "유저네임")
    val username: String,

    @Schema(description = "유저의 깃헙 ID")
    val github: String,

    @Schema(description = "JWT 토큰")
    val jwtToken: String,

    @Schema(description = "Refresh 토큰")
    val refreshToken: String
)