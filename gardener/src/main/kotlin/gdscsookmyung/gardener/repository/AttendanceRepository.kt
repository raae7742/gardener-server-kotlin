package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AttendanceRepository: JpaRepository<Attendance, Long>{
    fun findByAttendee(attendee: Attendee): List<Attendance>
}