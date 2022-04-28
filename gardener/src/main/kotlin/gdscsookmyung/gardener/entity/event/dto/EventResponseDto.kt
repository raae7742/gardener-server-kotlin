package gdscsookmyung.gardener.entity.event.dto

import com.fasterxml.jackson.annotation.JsonFormat
import gdscsookmyung.gardener.entity.event.Event
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class EventResponseDto (

    @Schema(description = "이벤트 ID")
    val id: Long,

    @Schema(description = "이벤트 이름")
    val name: String,

    @Schema(description = "이벤트 내용")
    val content: String?,

    @Schema(description = "이벤트 시작 날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val startedAt: LocalDate?,

    @Schema(description = "이벤트 종료 날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val endedAt: LocalDate?,

    @Schema(description = "참여자 수")
    val attendeesCount: Int
) {
    constructor(event: Event) : this(event.id!!, event.name!!, event.content, event.startedAt, event.endedAt, event.attendees.size)
}