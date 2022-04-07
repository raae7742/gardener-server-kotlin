package gdscsookmyung.gardener.entity.user.dto

import lombok.Data

@Data
data class UserRequestDto (
    val username: String,
    val password: String,
    val github: String,
)