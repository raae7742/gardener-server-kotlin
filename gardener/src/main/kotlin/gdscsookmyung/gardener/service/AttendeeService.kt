package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.repository.AttendeeRepository
import gdscsookmyung.gardener.repository.EventRepository
import gdscsookmyung.gardener.repository.UserRepository
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AttendeeService(
    val attendeeRepository: AttendeeRepository,
    val userRepository: UserRepository,
    val eventRepository: EventRepository,
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
        val attendee = attendeeRepository.findById(id).orElseThrow{throw CustomException(ErrorCode.NOT_FOUND)}
        attendeeRepository.delete(attendee)
    }

    fun delete(eventId: Long, username: String) {
        val event = eventRepository.findById(eventId).orElseThrow{ throw CustomException(ErrorCode.NOT_FOUND) }
        val user = userRepository.findByUsername(username).orElseThrow{ throw CustomException(ErrorCode.USER_NOT_FOUND) }
        val attendee = attendeeRepository.findByEventAndGithub(event, user.github).orElseThrow{ throw CustomException(ErrorCode.NOT_FOUND) }

        return attendeeRepository.delete(attendee)
    }
}