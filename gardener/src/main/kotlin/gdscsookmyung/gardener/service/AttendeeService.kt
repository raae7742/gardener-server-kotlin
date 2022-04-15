package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.repository.AttendeeRepository
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AttendeeService(
    val attendeeRepository: AttendeeRepository,
    val attendanceService: AttendanceService
) {
    fun create(github: String, event: Event): Attendee {
        val attendee = Attendee(
            github = github,
            event = event
        )
        attendeeRepository.save(attendee)

        var i = attendee.event!!.startedAt
        while (i!!.isBefore(attendee.event!!.endedAt)) {
            attendanceService.create(attendee, i)
            i = i.plusDays(1)
        }
        attendanceService.create(attendee, i)

        return attendee
    }

    fun readByEvent(event: Event): List<Attendee> {
        return attendeeRepository.findByEvent(event)
    }

    fun delete(id: Long) {
        val attendee: Attendee = attendeeRepository.findById(id).orElseThrow{throw CustomException(ErrorCode.NOT_FOUND)}
        attendeeRepository.delete(attendee)
    }
}