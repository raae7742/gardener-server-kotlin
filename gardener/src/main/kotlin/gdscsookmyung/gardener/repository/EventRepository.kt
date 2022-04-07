package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.event.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository: JpaRepository<Event, Long>{

}