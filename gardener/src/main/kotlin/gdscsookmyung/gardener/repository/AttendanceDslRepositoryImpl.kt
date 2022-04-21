package gdscsookmyung.gardener.repository

import com.querydsl.core.types.dsl.BooleanExpression
import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.attendance.QAttendance
import gdscsookmyung.gardener.entity.attendee.QAttendee
import gdscsookmyung.gardener.entity.event.QEvent
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class AttendanceDslRepositoryImpl: QuerydslCustomRepositorySupport(Attendance::class.java), AttendanceDslRepository {
    private val attendance: QAttendance = QAttendance("attendance")
    private val attendee: QAttendee = QAttendee("attendee")
    private val event: QEvent = QEvent("event")

    override fun findByEventAndDate(eventId: Long, date: LocalDate): MutableList<Attendance> {
        return selectFrom(attendance)
            .join(attendee).on(attendance.attendee.id.eq(attendee.id))
            .join(event).on(attendee.event.id.eq(event.id))
            .where(
                eventIdEq(eventId),
                dateEq(date),
            ).fetch()

    }

    private fun eventIdEq(eventId: Long): BooleanExpression {
        return event.id.eq(eventId)
    }

    private fun dateEq(date: LocalDate): BooleanExpression {
        return attendance.date.eq(date)
    }
}