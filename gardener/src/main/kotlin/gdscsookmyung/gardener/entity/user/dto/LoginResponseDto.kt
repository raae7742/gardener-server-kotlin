package gdscsookmyung.gardener.entity.user.dto

data class LoginResponseDto (
    val id: Long,
    val username: String,
    val github: String,
    val jwtToken: String,
    val refreshToken: String
)