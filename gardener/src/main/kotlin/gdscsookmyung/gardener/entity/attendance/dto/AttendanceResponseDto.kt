package gdscsookmyung.gardener.entity.attendance.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class AttendanceResponseDto(
    @Schema(description = "날짜")
    val date: LocalDate,
    @Schema(description = "출석 여부")
    val isChecked: Boolean,

): Comparable<AttendanceResponseDto> {

    override fun compareTo(other: AttendanceResponseDto): Int {
        return this.date.compareTo(other.date)
    }

}
