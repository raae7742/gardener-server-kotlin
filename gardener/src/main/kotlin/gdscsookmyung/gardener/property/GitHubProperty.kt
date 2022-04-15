package gdscsookmyung.gardener.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix="github")
@ConstructorBinding
data class GitHubProperty (
    val token: String,
)