package gdscsookmyung.gardener.entity.user.dto

data class TokenRequestDto (
    val accessToken: String,
    val refreshToken: String
)