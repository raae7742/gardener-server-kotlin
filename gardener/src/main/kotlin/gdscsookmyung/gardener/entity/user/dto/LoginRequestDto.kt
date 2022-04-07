package gdscsookmyung.gardener.entity.user.dto

import lombok.Data

@Data
data class LoginRequestDto (
    val username: String,
    val password: String,
)