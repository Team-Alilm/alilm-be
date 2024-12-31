package org.team_alilm.quartz.job.handler.impl

import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.team_alilm.application.service.NotificationService
import org.team_alilm.domain.product.Product
import org.team_alilm.global.util.StringContextHolder
import org.team_alilm.quartz.job.handler.PlatformHandler

@Component
class CM29Handler(
    private val notificationService: NotificationService,
    private val restClient: RestClient
) : PlatformHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun process(product: Product) {
        if (!isSoldOut(product)) {
            notificationService.sendNotifications(product)
        }
    }

    private fun isSoldOut(product: Product): Boolean {
        val productDetailJsonNode = getProductDetailJsonNode(product.number) ?: return true
        val optionItems = productDetailJsonNode.get("optionItems")

        if (optionItems == null || optionItems.size() == 0) {
            return productDetailJsonNode.get("itemStockStatus").asText() == "5"
        }

        val firstOption = optionItems.get("list")?.filter() {
            val title = it.get("title").asText()
            title == product.firstOption
        }?.first()

        if (firstOption?.get("optionStockStatus")?.asText() == "5" && firstOption.get("list")?.isEmpty == true) {
            return true
        } else if (firstOption?.get("list")?.isEmpty == false) {
            val secondOption = firstOption.get("list").first() {
                val title = it.get("title").asText()
                title == product.secondOption
            }

            return secondOption.get("optionStockStatus").asText() == "5"
        }

        return false
    }

    private fun getProductDetailJsonNode(productNumber: Long): JsonNode? {
        try {
            return restClient.get()
                .uri(StringContextHolder.CM29_PRODUCT_DETAIL_API_URL.get().format(productNumber))
                .retrieve()
                .body(JsonNode::class.java)
                ?.get("data")
        } catch (e : Exception) {
            log.error("Failed to fetch product detail from 29cm", e)
            return null
        }
    }
}