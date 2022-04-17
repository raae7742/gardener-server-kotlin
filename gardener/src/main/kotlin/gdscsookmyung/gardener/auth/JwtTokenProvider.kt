package gdscsookmyung.gardener.auth

import gdscsookmyung.gardener.property.JwtProperty
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    private val jwtProperty: JwtProperty,
    private var secretKey: String,
    private val userDetailsService: UserDetailsService,
    private val LOG: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
) {

    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperty.secretKey.toByteArray())
    }

    fun createToken(email: String, role: RoleType): String {
        val claims = Jwts.claims().setSubject(email)
        claims.put("role", role)

        val now = Date()
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + jwtProperty.accessTime))
            .signWith(SignatureAlgorithm.ES256, secretKey)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUserEmail(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserEmail(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(request: HttpServletRequest): String {
        return request.getHeader("Authorization")
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            LOG.error("Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            LOG.error("Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            LOG.error("Expired JWT token")
        } catch (e: UnsupportedJwtException) {
            LOG.error("Unsupported JWT token")
        } catch (e: IllegalArgumentException) {
            LOG.error("JWT claims string is empty")
        }
        return false
    }
}