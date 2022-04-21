package gdscsookmyung.gardener

import gdscsookmyung.gardener.property.GitHubProperty
import gdscsookmyung.gardener.property.JwtProperty
import gdscsookmyung.gardener.property.SlackProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(JwtProperty::class, GitHubProperty::class, SlackProperty::class)
class GardenerApplication

fun main(args: Array<String>) {
	runApplication<GardenerApplication>(*args)
}
