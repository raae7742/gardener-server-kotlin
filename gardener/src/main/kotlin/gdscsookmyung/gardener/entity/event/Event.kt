package gdscsookmyung.gardener.entity.event

import gdscsookmyung.gardener.entity.Timestamped
import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.dto.EventRequestDto
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDate
import javax.persistence.*

@Entity
@Getter
@NoArgsConstructor
class Event(

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(length = 20)
    var name: String? = null,

    @Column
    var startedAt: LocalDate? = null,

    @Column
    var endedAt: LocalDate? = null,

    @Column(length = 45)
    var content: String? = null,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL])
    var attendees: MutableList<Attendee> = mutableListOf(),

): Timestamped() {

    constructor(requestDto: EventRequestDto): this() {
        this.name = requestDto.name
        this.content = requestDto.content
        this.startedAt = requestDto.startedAt
        this.endedAt = requestDto.endedAt
    }

    fun update(requestDto: EventRequestDto) {
        this.name = requestDto.name
        this.content = requestDto.content
        this.startedAt = requestDto.startedAt
        this.endedAt = requestDto.endedAt
    }

}