package gdscsookmyung.gardener.scheduling

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SlackScheduleTasksTest(
    @Autowired private val slackScheduleTasks: SlackScheduleTasks,
) {

    @Test
    fun `10시마다 오늘 출석 현황 메시지 전송`() {
        slackScheduleTasks.sendTodayAttendances()
    }

    @Test
    fun `전체 출석 현황 메시지 전송`() {
        slackScheduleTasks.sendAllAttendances()
    }
}