package gdscsookmyung.gardener.service

import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserServiceTest(
    private val userService: UserService,
    private val eventService: EventService
) {
    private val eventStack = Stack<Long>()

    @AfterEach
    fun afterEach() {
        while(!eventStack.isEmpty()) eventService.delete(eventStack.pop())
    }

}