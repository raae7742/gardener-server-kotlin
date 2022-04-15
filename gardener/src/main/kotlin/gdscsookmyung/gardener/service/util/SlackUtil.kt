package gdscsookmyung.gardener.service.util

import com.slack.api.Slack
import com.slack.api.methods.SlackApiException
import com.slack.api.model.Message
import gdscsookmyung.gardener.property.SlackProperty
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SlackUtil(
    val slackProperty: SlackProperty,
    var conversationHistory: List<Message> = emptyList()
) {
    fun fetchHistory() {
        val client = Slack.getInstance().methods()
        val logger = LoggerFactory.getLogger("slack-app")

        try {
            val result = client.conversationsHistory { r -> r.token(slackProperty.token).channel(slackProperty.id) }
            conversationHistory = result.messages
            logger.info("{} messages found in {}",conversationHistory.size,slackProperty.id)
        } catch(e: IOException) {
            logger.error("error: {}", e.message, e)
        } catch(e: SlackApiException) {
            logger.error("error: {}", e.message, e)
        }
    }
}