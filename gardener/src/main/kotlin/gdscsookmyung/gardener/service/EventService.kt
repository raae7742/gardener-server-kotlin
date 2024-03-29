package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import gdscsookmyung.gardener.entity.event.dto.EventResponseDto
import gdscsookmyung.gardener.entity.user.User
import gdscsookmyung.gardener.repository.AttendeeRepository
import gdscsookmyung.gardener.repository.EventRepository
import gdscsookmyung.gardener.repository.UserRepository
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class EventService (
    val eventRepository: EventRepository,
    val attendeeRepository: AttendeeRepository,
    val userRepository: UserRepository,
) {

    fun create(requestDto: EventRequestDto): EventResponseDto {
        var event = Event(requestDto)
        event = eventRepository.save(event)

        for (githubId in requestDto.attendees) {
            val attendee = Attendee(github = githubId, event = event)
            event.attendees.add(attendee)
            attendeeRepository.save(attendee)
        }

        event = eventRepository.save(event)
        return EventResponseDto(event)
    }

    @Transactional
    fun readById(id: Long): EventResponseDto {
        val event = eventRepository.findById(id).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
        return EventResponseDto(event)
    }

    @Transactional
    fun readCurrentEvents(): List<EventResponseDto> {
        val currentEvents = eventRepository.findCurrentEvents()
        return convertToResponseDtoList(currentEvents)
    }

    @Transactional
    fun readPastEvents(): List<EventResponseDto> {
        val pastEvents = eventRepository.findPastEvents()
        return convertToResponseDtoList(pastEvents)
    }

    fun readFutureEvents(): List<EventResponseDto> {
        val futureEvents = eventRepository.findFutureEvents()
        return convertToResponseDtoList(futureEvents)
    }

    fun readUserEvents(username: String): List<EventResponseDto> {
        val user: User = userRepository.findByUsername(username).orElseThrow { throw CustomException(ErrorCode.USER_NOT_FOUND) }
        val attendees = attendeeRepository.findAllByGithub(user.github)

        val events = mutableListOf<Event>()
        attendees.forEach { events.add(it.event!!) }

        return convertToResponseDtoList(events)
    }

    private fun convertToResponseDtoList(currentEvents: List<Event>): List<EventResponseDto> {
        val response: MutableList<EventResponseDto> = mutableListOf()
        for (e in currentEvents) response.add(EventResponseDto(e))
        return response
    }

    fun update(id: Long, requestDto: EventRequestDto): EventResponseDto {
        val event = eventRepository.findById(id).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
        event.update(requestDto)
        eventRepository.save(event)

        return EventResponseDto(event)
    }

    fun delete(id: Long) {
        val event = eventRepository.findById(id).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
        eventRepository.delete(event)
    }
}