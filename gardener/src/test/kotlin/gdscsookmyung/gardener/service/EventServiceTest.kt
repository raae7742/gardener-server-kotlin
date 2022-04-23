package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import gdscsookmyung.gardener.repository.EventRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*

@SpringBootTest
internal class EventServiceTest (
    private val eventService: EventService,
    private val eventRepository: EventRepository
) {
    private val eventStack = Stack<Long>()

    @AfterEach
    fun afterEach() {
        while(!eventStack.isEmpty()) eventService.delete(eventStack.pop())
    }

    @Test
    fun create() {
        //given
        val startedAt = LocalDate.of(2022, 4, 23)
        val endedAt = LocalDate.of(2022, 4, 24)
        val attendees = listOf<String>("raae7742", "mori8")

        val requestDto = EventRequestDto(
            name = "Event1",
            content = "Content1",
            startedAt = startedAt,
            endedAt = endedAt,
            attendees = attendees
        )

        //when
        val event = eventService.create(requestDto)
        eventStack.add(event.id)

        //then
        val findEvent = eventService.readById(event.id)
        assertThat(findEvent.id).isEqualTo(event.id)
        assertThat(findEvent.startedAt).isEqualTo(event.startedAt)
        assertThat(findEvent.attendees.size).isEqualTo(2)
    }

}