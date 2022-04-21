package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendee.Attendee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AttendanceRepository: JpaRepository<Attendance, Long>, AttendanceDslRepository {
    fun findByAttendee(attendee: Attendee): List<Attendance>

    fun findByAttendeeAndDate(attendee: Attendee, date: LocalDate): Attendance
}