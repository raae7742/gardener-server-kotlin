package gdscsookmyung.gardener.service

import gdscsookmyung.gardener.repository.EventRepository
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class EventServiceTest (
    private val eventService: EventService,
    private val eventRepository: EventRepository
) {

}