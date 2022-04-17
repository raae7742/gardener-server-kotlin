package gdscsookmyung.gardener.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix="slack")
@ConstructorBinding
data class JwtProperty (
    val secretKey: String,
    val accessTime: Int,
)