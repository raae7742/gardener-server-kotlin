package gdscsookmyung.gardener.entity.user

import com.fasterxml.jackson.annotation.JsonIgnore
import gdscsookmyung.gardener.entity.Timestamped
import gdscsookmyung.gardener.entity.attendee.Attendee
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
class User(

    @Id
    @Column(name = "user_id", length = 20)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(length = 20)
    var username: String? = null,

    @JsonIgnore
    @Column(length = 20)
    var password: String? = null,

    @Column(length = 20)
    var github: String? = null,

    var role: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var attendees: MutableList<Attendee> = mutableListOf(),

): Timestamped() {
    fun addAttendee(attendee: Attendee) {
        attendees.add(attendee)
    }
}