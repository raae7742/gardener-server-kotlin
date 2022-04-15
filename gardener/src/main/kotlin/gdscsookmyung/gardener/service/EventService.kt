package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import gdscsookmyung.gardener.entity.event.dto.EventResponseDto
import gdscsookmyung.gardener.repository.EventRepository
import gdscsookmyung.gardener.util.exception.CustomException
import gdscsookmyung.gardener.util.exception.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class EventService (
    val eventRepository: EventRepository,
    val attendeeService: AttendeeService,
) {

    fun create(requestDto: EventRequestDto): EventResponseDto {
        var event = Event(requestDto)
        event = eventRepository.save(event)

        for (a in requestDto.attendees) {
            val attendee = attendeeService.create(a, event)
            event.attendees.add(attendee)
        }
        event = eventRepository.save(event)

        return EventResponseDto(event)
    }

    fun readById(id: Long): Event {
        return eventRepository.findById(id).orElseThrow { CustomException(ErrorCode.NOT_FOUND) }
    }

    fun readCurrentEvents(): List<EventResponseDto> {
        val currentEvents = eventRepository.findCurrentEvents()
        return convertToResponseDtoList(currentEvents)
    }

    fun readPastEvents(): List<EventResponseDto> {
        val pastEvents = eventRepository.findPastEvents()
        return convertToResponseDtoList(pastEvents)
    }

    fun readFutureEvents(): List<EventResponseDto> {
        val futureEvents = eventRepository.findFutureEvents()
        return convertToResponseDtoList(futureEvents)
    }

    private fun convertToResponseDtoList(currentEvents: List<Event>): List<EventResponseDto> {
        val response: MutableList<EventResponseDto> = mutableListOf()
        for (e in currentEvents) response.add(EventResponseDto(e))
        return response
    }

    fun update(id: Long, requestDto: EventRequestDto): EventResponseDto {
        val event = readById(id)
        event.update(requestDto)
        eventRepository.save(event)

        return EventResponseDto(event)
    }

    fun delete(id: Long) {
        val event = readById(id)
        eventRepository.delete(event)
    }
}