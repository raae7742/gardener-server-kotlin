package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.attendance.Attendance
import java.time.LocalDate

interface AttendanceDslRepository {
    fun findByEventAndDate(eventId: Long, date: LocalDate) : MutableList<Attendance>
}