package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.attendee.Attendee
import gdscsookmyung.gardener.entity.event.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttendeeRepository: JpaRepository<Attendee, Long> {

    fun findByEvent(event: Event): List<Attendee>

}