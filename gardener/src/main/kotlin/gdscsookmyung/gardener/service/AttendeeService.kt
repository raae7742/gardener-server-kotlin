package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendee.Attendee
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
) {
    fun create(eventId: Long, github: String): Attendee {
        val event = eventRepository.findById(eventId).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
        val attendee = Attendee(github = github, event = event)
        return attendeeRepository.save(attendee)
    }

    fun readByEventId(eventId: Long): List<Attendee> {
        val event = eventRepository.findById(eventId).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
        return attendeeRepository.findAllByEvent(event)
    }

    fun delete(eventId: Long, githubId: String) {
        val event = eventRepository.findById(eventId).orElseThrow{ throw CustomException(ErrorCode.NOT_FOUND) }
        val user = userRepository.findByUsername(githubId).orElseThrow{ throw CustomException(ErrorCode.USER_NOT_FOUND) }
        val attendee = attendeeRepository.findByEventAndGithub(event, user.github).orElseThrow{ throw CustomException(ErrorCode.NOT_FOUND) }

        return attendeeRepository.delete(attendee)
    }
}