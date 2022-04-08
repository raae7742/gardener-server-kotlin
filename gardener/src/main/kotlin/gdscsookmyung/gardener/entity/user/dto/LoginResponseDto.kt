package gdscsookmyung.gardener.entity.user.dto

data class LoginResponseDto (
    val username: String,
    val github: String,
    val jwtToken: String,
    val refreshToken: String
)