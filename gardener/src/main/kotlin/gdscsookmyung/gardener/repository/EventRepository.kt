package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.event.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EventRepository: JpaRepository<Event, Long>{
    @Query(value = "SELECT * FROM event e WHERE ended_at > now() AND started_at < now()", nativeQuery = true)
    fun findCurrentEvents(): List<Event>

    @Query(value = "SELECT * FROM event e WHERE ended_at < now()", nativeQuery = true)
    fun findPastEvents(): List<Event>

    @Query(value = "SELECT * FROM event e WHERE started_at > now()", nativeQuery = true)
    fun findFutureEvents(): List<Event>
}