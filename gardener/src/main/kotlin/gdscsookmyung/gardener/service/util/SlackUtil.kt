package gdscsookmyung.gardener.service.util

import com.slack.api.Slack
import com.slack.api.methods.SlackApiException
import com.slack.api.model.Message
import gdscsookmyung.gardener.property.SlackProperty
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.config.annotation.UrlBasedViewResolverRegistration
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

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

    fun sendMessage(message: String) {
        var requestUrl = "https://slack.com/api/chat.postMessage?"
        requestUrl += "token=" + slackProperty.token + "&"
        requestUrl += "channel=" +  slackProperty.id
        requestUrl += "text=" + URLEncoder.encode(message, "UTF-8")

        val conn: HttpURLConnection = URL(requestUrl).openConnection() as HttpURLConnection
        conn.setRequestProperty("Accept", "application/json")
        conn.requestMethod = "GET"
        conn.connect()

        BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
    }
}