package gdscsookmyung.gardener.entity.user

import com.fasterxml.jackson.annotation.JsonIgnore
import gdscsookmyung.gardener.auth.RoleType
import gdscsookmyung.gardener.entity.Timestamped
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Getter
@Setter
@AllArgsConstructor
class User(
    @Column(length = 20)
    private var username: String,

    @JsonIgnore
    @Column
    private var password: String,

    @Column(length = 20)
    var github: String,

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    var role: RoleType,

): Timestamped(), UserDetails {

    @Id
    @Column(name = "user_id", length = 20)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun update(username: String) {
        this.username = username
    }
}