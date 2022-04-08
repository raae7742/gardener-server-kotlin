package gdscsookmyung.gardener.entity.user.dto

data class UserRequestDto (
    val username: String,
    val password: String,
    val github: String,
)