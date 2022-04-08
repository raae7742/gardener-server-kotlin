package gdscsookmyung.gardener.entity.attendance

import gdscsookmyung.gardener.entity.Timestamped
import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.util.response.BooleanToYNConverter
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDate
import javax.persistence.*

@Entity
@Getter
@NoArgsConstructor
class Attendance(

    @Id
    @Column(name = "attendance_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id")
    var attendee: Attendee? = null,

    @Column
    var date: LocalDate = LocalDate.now(),

    @Convert(converter = BooleanToYNConverter::class)
    @Column(length = 1)
    var isChecked: Boolean = false,

    @Convert(converter = BooleanToYNConverter::class)
    @Column(length = 1)
    var commit: Boolean = false,

    @Convert(converter = BooleanToYNConverter::class)
    @Column(length = 1)
    var til: Boolean = false,

) : Timestamped(), Comparable<Attendance> {

    override fun compareTo(other: Attendance): Int {
       return this.date.compareTo(other.date)
    }



}