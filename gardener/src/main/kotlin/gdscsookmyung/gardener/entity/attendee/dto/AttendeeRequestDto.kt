package gdscsookmyung.gardener.entity.attendee.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AttendeeRequestDto(
    @Schema(description = "깃허브 ID")
    val github: String
)
