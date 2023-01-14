package gdscsookmyung.gardener.scheduling

import gdscsookmyung.gardener.entity.attendance.dto.AttendanceDateDto
import gdscsookmyung.gardener.service.AttendanceService
import gdscsookmyung.gardener.service.AttendeeService
import gdscsookmyung.gardener.service.EventService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SlackScheduleTasks(
    private val eventService: EventService,
    private val attendeeService: AttendeeService,
    private val attendanceService: AttendanceService,
) {

    @Scheduled(cron = "0 0 22 * * *")
    fun sendTodayAttendances() {
        val eventId = 1L

        // 10시마다 오늘의 출석 현황 슬랙에 보내기
        val attendances = attendanceService.readAllByEventIdAndDate(eventId, LocalDate.now())
        val attendeeList = attendeeService.readByEventId(eventId)

        val result = mutableListOf<AttendanceDateDto>()
        for (attendee in attendeeList) {
            var isChecked = false
            for (a in attendances) {
                if (a.github == attendee.github) {
                    isChecked = true
                }
            }

            if (isChecked) result.add(AttendanceDateDto(attendee.github!!, true))
            else result.add(AttendanceDateDto(attendee.github!!, false))
        }

        for (a in result) {
            println(a.github + ": " + a.isChecked)
        }
    }

    fun sendAllAttendances() {
        val eventId = 1L
        val attendances = attendanceService.readAllByEventId(eventId)

        for (a in attendances) {
            println(a.github)

            for (i in a.attendances) {
                println("  " + i.date + ": " + i.isChecked)
            }
        }
    }
}