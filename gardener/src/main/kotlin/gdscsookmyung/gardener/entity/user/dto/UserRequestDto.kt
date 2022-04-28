package gdscsookmyung.gardener.entity.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class UserRequestDto (

    @Schema(description = "유저네임")
    val username: String,

    @Schema(description = "패스워드")
    val password: String,

    @Schema(description = "유저의 깃헙 ID")
    val github: String,
)