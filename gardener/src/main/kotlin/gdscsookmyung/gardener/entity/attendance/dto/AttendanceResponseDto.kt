package gdscsookmyung.gardener.entity.attendance.dto

import java.time.LocalDate

data class AttendanceResponseDto(
    val date: LocalDate,
    val isChecked: Boolean,

): Comparable<AttendanceResponseDto> {

    override fun compareTo(other: AttendanceResponseDto): Int {
        return this.date.compareTo(other.date)
    }

}
