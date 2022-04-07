package gdscsookmyung.gardener.repository

import gdscsookmyung.gardener.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> {

    fun existsByGithub(github: String): Boolean

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): Optional<User>
}