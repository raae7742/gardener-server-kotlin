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
    val attendeeRepository: AttendeeRepository
) {
    fun create(github: String, event: Event): Attendee {
        val attendee = Attendee(
            github = github,
            event = event
        )

        return attendeeRepository.save(attendee)
    }

    fun readByEvent(event: Event): List<Attendee> {
        return attendeeRepository.findByEvent(event)
    }

    fun delete(id: Long) {
        val attendee: Attendee = attendeeRepository.findById(id).orElseThrow{throw CustomException(ErrorCode.NOT_FOUND)}
        attendeeRepository.delete(attendee)
    }
}