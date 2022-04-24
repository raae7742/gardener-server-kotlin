package gdscsookmyung.gardener.service

import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


@SpringBootTest
class AttendanceServiceTest(
    private val attendanceService: AttendanceService,
    private val attendeeService: AttendeeService,
    private val eventService: EventService,
) {

    private val eventStack = Stack<Long>()

    @AfterEach
    fun afterEach() {
        while(!eventStack.isEmpty()) eventService.delete(eventStack.pop())
    }

}