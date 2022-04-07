package gdscsookmyung.gardener.entity.attendance.dto

import lombok.Data
import java.time.LocalDate

@Data
data class AttendanceResponseDto(
    val date: LocalDate,
    val isChecked: Boolean,

): Comparable<AttendanceResponseDto> {

    override fun compareTo(other: AttendanceResponseDto): Int {
        return this.date.compareTo(other.date)
    }

}
