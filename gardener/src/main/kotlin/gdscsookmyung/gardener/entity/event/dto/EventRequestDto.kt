package gdscsookmyung.gardener.entity.event.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class EventRequestDto (
    @Schema(description = "이벤트 이름")
    val name: String,
    @Schema(description = "이벤트 설명")
    val content: String,

    @Schema(description = "이벤트 시작 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startedAt: LocalDate,

    @Schema(description = "이벤트 종료 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endedAt: LocalDate,

    @Schema(description = "참여자들의 깃헙 ID")
    val attendees: List<String>
        )