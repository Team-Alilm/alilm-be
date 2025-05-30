package org.team_alilm.application.service

import domain.product.Product
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.team_alilm.application.handler.PlatformHandlerResolver
import org.team_alilm.gateway.SendSlackGateway

@Service
class ProductSoldoutCheckService(
    private val restClient: RestClient,
    private val platformHandlerResolver: PlatformHandlerResolver,
    private val slackGateway: SendSlackGateway,
    @Value("\${jwt-token}") private val jwtToken: String
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @SqsListener("product-soldout-check-queue")
    fun checkSoldout(payload: Product, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        try {
            log.info("Received message: productId = ${payload.id}, store = ${payload.store}")

            val handle = platformHandlerResolver.resolve(payload.store)
            val restock = handle.process(payload)

            if (restock) {
                slackGateway.sendMessage("${payload.id} ${payload.getStoreUrl()} 재입고 되었습니다.")

                val requestBody = RequestBody(productId = payload.id!!)
                restClient.put()
                    .uri("https://api.algamja.com/api/v1/baskets/alilm")
                    .header("authorization", jwtToken)
                    .body(requestBody)
                    .retrieve()
            }
        } catch (e: Exception) {
            log.error("Error occurred while processing message: productId = ${payload.id}", e)
            slackGateway.sendMessage("Error occurred while processing message: productId = ${payload.id}")
        } finally {
            acknowledgement.acknowledge()
        }
    }

    data class RequestBody(
        val productId: Long
    )
}