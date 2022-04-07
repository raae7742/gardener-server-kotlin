package gdscsookmyung.gardener.entity.event

import gdscsookmyung.gardener.entity.Timestamped
import gdscsookmyung.gardener.entity.attendee.Attendee
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDate
import javax.persistence.*

@Entity
@Getter
@NoArgsConstructor
class Event: Timestamped() {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(length = 20)
    var name: String? = null

    @Column
    var startedAt: LocalDate? = null

    @Column
    var endedAt: LocalDate? = null

    @Column(length = 45)
    var content: String? = null

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL])
    var attendees: MutableList<Attendee> = mutableListOf()

}