package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import gdscsookmyung.gardener.repository.EventRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.*

@SpringBootTest
internal class EventServiceTest (
    private val eventService: EventService,
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

    @Test
    fun findPastEvents() {
        //given
        createPastEvent()
        createCurrentEvent()
        createFutureEvent()

        //when
        val pastEvents = eventService.readPastEvents()

        //then
        for (e in pastEvents) {
            assertThat((e.endedAt)).isBefore(LocalDate.now())
        }
    }

    @Test
    fun findCurrentEvents() {
        //given
        createPastEvent()
        createCurrentEvent()
        createFutureEvent()

        //when
        val currentEvents = eventService.readCurrentEvents()

        //then
        for (e in currentEvents) {
            assertThat((e.startedAt)).isBefore(LocalDate.now())
            assertThat((e.endedAt)).isAfterOrEqualTo(LocalDate.now())
        }
    }

    @Test
    fun findFutureEvents() {
        //given
        createPastEvent()
        createCurrentEvent()
        createFutureEvent()

        //when
        val currentEvents = eventService.readFutureEvents()

        //then
        for (e in currentEvents) {
            assertThat((e.startedAt)).isAfter(LocalDate.now())
        }
    }

    private fun createPastEvent() {
        val startedAt = LocalDate.now().minusDays(3)
        val endedAt = LocalDate.now().minusDays(1)
        val attendees = listOf("raae7742", "mori8")

        val requestDto = EventRequestDto(
            name = "Past Event",
            content = "Past Content",
            startedAt = startedAt,
            endedAt = endedAt,
            attendees = attendees
        )

        val event = eventService.create(requestDto)
        eventStack.add(event.id)
    }

    private fun createCurrentEvent() {
        val startedAt = LocalDate.now().minusDays(2)
        val endedAt = LocalDate.now().plusDays(2)
        val attendees = listOf("raae7742", "mori8")

        val requestDto = EventRequestDto(
            name = "Current Event",
            content = "Current Content",
            startedAt = startedAt,
            endedAt = endedAt,
            attendees = attendees
        )

        val event = eventService.create(requestDto)
        eventStack.add(event.id)
    }

    private fun createFutureEvent() {
        val startedAt = LocalDate.now().plusDays(2)
        val endedAt = LocalDate.now().plusDays(4)
        val attendees = listOf("raae7742", "mori8")

        val requestDto = EventRequestDto(
            name = "Future Event",
            content = "Future Content",
            startedAt = startedAt,
            endedAt = endedAt,
            attendees = attendees
        )

        val event = eventService.create(requestDto)
        eventStack.add(event.id)
    }

}