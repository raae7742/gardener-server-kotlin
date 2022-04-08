package gdscsookmyung.gardener.entity.event.dto

import com.fasterxml.jackson.annotation.JsonFormat
import gdscsookmyung.gardener.entity.event.Event
import java.time.LocalDate

data class EventResponseDto (
    val id: Long,
    val name: String,
    val content: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val startedAt: LocalDate?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    val endedAt: LocalDate?,
    val attendeesCount: Int
) {
    constructor(event: Event) : this(event.id!!, event.name!!, event.content, event.startedAt, event.endedAt, event.attendees.size)
}