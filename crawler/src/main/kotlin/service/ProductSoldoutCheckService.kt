package org.team_alilm.service

import domain.product.Product
import domain.product.ProductId
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.slf4j.LoggerFactory
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.team_alilm.handler.PlatformHandlerResolver

@Service
class ProductSoldoutCheckService(
    private val restClient: RestClient,
    private val platformHandlerResolver: PlatformHandlerResolver,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @SqsListener("product-soldout-check-queue")
    fun checkSoldout(payload: Product, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        log.info("Received message: $payload")
        val handle = platformHandlerResolver.resolve(payload.store)
        val soldoutProduct = handle.process(payload)

        if (soldoutProduct) {
            val requestBody = RequestBody(productId = payload.id!!)
            val response = restClient.put()
                .uri("https://alilm.store/api/v1/baskets/alilm")
                .body(requestBody)
                .retrieve()
                .body(String::class.java)

            log.info("Response: $response")
        }

        acknowledgement.acknowledge()
    }

    data class RequestBody(
        val productId: ProductId
    )
}