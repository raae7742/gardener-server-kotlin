package gdscsookmyung.gardener.entity.attendance.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AttendanceDateDto(
    @Schema(description = "참여자의 깃헙 ID")
    val github: String,
    @Schema(description = "참여자의 해당 날짜 출석 현황")
    val isChecked: Boolean,
)
