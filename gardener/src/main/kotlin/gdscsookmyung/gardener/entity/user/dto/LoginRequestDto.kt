package gdscsookmyung.gardener.entity.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class LoginRequestDto (

    @Schema(description = "유저네임")
    val username: String,

    @Schema(description = "비밀번호")
    val password: String,
)