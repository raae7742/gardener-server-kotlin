package gdscsookmyung.gardener.entity.attendance.dto

import lombok.Data

@Data
data class AttendanceDateDto(
    val github: String,
    val isChecked: Boolean,
)
