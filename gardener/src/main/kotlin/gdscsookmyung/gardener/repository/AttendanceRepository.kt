package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendee.Attendee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AttendanceRepository: JpaRepository<Attendance, Long>{
    fun findByAttendee(attendee: Attendee): List<Attendance>

    @Query(value = "select ac from Attendance ac " +
            "fetch join Attendee ae on ac.attendee_id = ae.attendee_id " +
            "where ae.event_id = :event_id and ac.date = :date", nativeQuery = true)
    fun findByEventAndDate(@Param("event_id") event_id: Long,@Param("date") date: LocalDate): List<Attendance>
}