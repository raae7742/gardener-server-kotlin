package gdscsookmyung.gardener.entity.event.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class EventRequestDto (
    val name: String,
    val content: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startedAt: LocalDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endedAt: LocalDate,
    val attendees: List<String>
        )