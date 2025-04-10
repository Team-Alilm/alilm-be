package org.team_alilm.gateway.impl

import com.slack.api.Slack
import domain.product.Product
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.team_alilm.gateway.SendSlackGateway

@Service
class SlackGateway(
    @Value("\${webhook.slack.url}")
    private val SLACK_NOTICE_CH_WEBHOOK_URL: String,
) : SendSlackGateway {

    private val slackClient: Slack = Slack.getInstance()
        ?: throw IllegalStateException("Slack client is not initialized")

    override fun sendMessage(message: String) {
        val payload = """
            {
                "text": "$message"
            }
        """.trimIndent()

        slackClient.send(SLACK_NOTICE_CH_WEBHOOK_URL, payload)
    }

    override fun sendMessage(product: Product) {
        slackClient.send(SLACK_NOTICE_CH_WEBHOOK_URL, product.toSlackMessage())
    }
}