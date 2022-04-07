package gdscsookmyung.gardener.entity.attendee

import gdscsookmyung.gardener.entity.Timestamped
import gdscsookmyung.gardener.entity.attendance.Attendance
import gdscsookmyung.gardener.entity.event.Event
import gdscsookmyung.gardener.entity.user.User
import lombok.Getter
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@Getter
@NoArgsConstructor
class Attendee(

    @Id
    @Column(name = "attendee_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

//    @Column(length = 20)
//    var name: String? = null,

    @Column(length = 20)
    var github: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    var event: Event? = null,

    @OneToMany(mappedBy = "attendee", cascade = [CascadeType.ALL])
    var attendances: MutableList<Attendance> = mutableListOf(),

): Timestamped() {
}