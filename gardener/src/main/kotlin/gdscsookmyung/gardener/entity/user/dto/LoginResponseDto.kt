package gdscsookmyung.gardener.entity.user.dto

import lombok.Data

@Data
data class LoginResponseDto (
    val username: String,
    val github: String,
    val jwtToken: String,
    val refreshToken: String
)